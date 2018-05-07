package org.hotrod.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import org.apache.log4j.Logger;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.utils.ClassPackage;
import org.hotrod.utils.Correlator;
import org.hotrod.utils.Correlator.CorrelatedEntry;

/**
 * <pre>
 *
 *     AbstractConfigurationTag <------------------------------------+
 *      ^                                                            |
 *      |                                                            |
 *     AbstractDAOTag <------------.............             AbstractMethodTag <-----------+
 *      ^                         ^            .                ^         ^                |
 *      |                         |            .                |         |                |
 *     AbstractEntityDAOTag  ExecutorDAOTag  SelectTag  QueryMethodTag  SelectMethodTag  SequenceMethodTag
 *      ^       ^        ^      
 *      |       |        |      
 *   TableTag  ViewTag  EnumTag
 * 
 * </pre>
 */

public abstract class AbstractDAOTag extends AbstractConfigurationTag implements GenerationUnit<AbstractDAOTag> {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = Logger.getLogger(AbstractDAOTag.class);

  // Properties

  private MethodTagContainer<SequenceMethodTag> sequences = new MethodTagContainer<SequenceMethodTag>();
  private MethodTagContainer<QueryMethodTag> queries = new MethodTagContainer<QueryMethodTag>();
  private MethodTagContainer<SelectMethodTag> selects = new MethodTagContainer<SelectMethodTag>();

  protected LinkedHashSet<String> declaredMethodNames = new LinkedHashSet<String>();

  // Constructor

  protected AbstractDAOTag(final String tagName) {
    super(tagName);
  }

  // JAXB Setters

  @XmlElement
  public final void setSequence(final SequenceMethodTag sequence) {
    this.sequences.add(sequence);
  }

  @XmlElement
  public final void setQuery(final QueryMethodTag query) {
    this.queries.add(query);
  }

  @XmlElement
  public final void setSelect(final SelectMethodTag select) {
    this.selects.add(select);
  }

  // Duplication

  protected void copyCommon(final AbstractDAOTag source) {
    super.copyCommon(source);

    // TODO: remove these once tested
    // this.sequences.copyMethods(source.sequences);
    // this.queries.copyMethods(source.queries);
    // this.selects.copyMethods(source.selects);
    // this.declaredMethodNames.addAll(source.declaredMethodNames);

  }

  // Behavior

  protected void validate(final DaosTag daosTag, final HotRodConfigTag config,
      final HotRodFragmentConfigTag fragmentConfig) throws InvalidConfigurationFileException {

    // sequences

    Set<String> seqNames = new HashSet<String>();

    for (SequenceMethodTag s : this.sequences) {
      s.validate();
      super.subTags.add(s);
      if (seqNames.contains(s.getName())) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(),
            "Duplicate sequence with name '" + s.getName() + "'.");
      }
      String method = s.getMethod();
      if (this.declaredMethodNames.contains(method)) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(),
            "Duplicate sequence method-name '" + method + "'.");
      }
      seqNames.add(s.getName());
      this.declaredMethodNames.add(method);
      log.debug("* added '" + method + "'");
    }

    // queries

    for (QueryMethodTag q : this.queries) {
      q.validate(daosTag, config, fragmentConfig);
      super.subTags.add(q);
      if (this.declaredMethodNames.contains(q.getMethod())) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(),
            "Duplicate java-method-name '" + q.getMethod()
                + "'. cannot add multiple queries or sequences with identical java-method-name "
                + "(specified or implied) in the same <dao> tag. " + "For <query> tags they cannot have the same name, "
                + "even if they have different parameters (different signature).");
      }
      this.declaredMethodNames.add(q.getMethod());
      log.debug("* added '" + q.getMethod() + "'");
    }

    // selects

    Set<String> methodNames = new HashSet<String>();

    for (SelectMethodTag s : this.selects) {
      s.validate(daosTag, config, fragmentConfig);
      super.subTags.add(s);

      if (methodNames.contains(s.getMethod())) {
        throw new InvalidConfigurationFileException(s.getSourceLocation(),
            "Duplicate method name '" + s.getMethod() + "' on <" + s.getTagName() + "> tag.");
      }
      methodNames.add(s.getMethod());

    }

  }

  // Getters

  public final List<AbstractMethodTag<?>> getMethods() {
    List<AbstractMethodTag<?>> methods = new ArrayList<AbstractMethodTag<?>>();
    methods.addAll(this.sequences.toList());
    methods.addAll(this.queries.toList());
    methods.addAll(this.selects.toList());
    return methods;
  }

  public final List<SequenceMethodTag> getSequences() {
    return sequences.toList();
  }

  public final List<QueryMethodTag> getQueries() {
    return queries.toList();
  }

  public List<SelectMethodTag> getSelects() {
    return selects.toList();
  }

  public final Set<String> getDeclaredMethodNames() {
    log.debug("get methods.");
    return this.declaredMethodNames;
  }

  // Update generated cache

  public boolean concludeGeneration(final AbstractDAOTag cache, final DatabaseAdapter adapter) {

    log.debug("----> DAO " + this.getJavaClassName() + " 1- generate="+this.getGenerate());

    boolean failedInnerGeneration = false;

    failedInnerGeneration |= markMethodGenerated(this.sequences, cache.sequences);
    failedInnerGeneration |= markMethodGenerated(this.queries, cache.queries);
    failedInnerGeneration |= markMethodGenerated(this.selects, cache.selects);
    log.debug("failedInnerGeneration=" + failedInnerGeneration);

    if (!failedInnerGeneration) {
      return this.concludeGenerationMarkTag();
    }

    return !failedInnerGeneration;

  }

  private <M extends AbstractMethodTag<M>> boolean markMethodGenerated(final MethodTagContainer<M> thisMethods,
      final MethodTagContainer<M> cache) {
    boolean failedInnerGeneration = false;
    log.debug("====> DAO " + this.getJavaClassName() + " 2");
    for (CorrelatedEntry<M> cor : Correlator.correlate(thisMethods.toList(), cache.toList(), new Comparator<M>() {
      @Override
      public int compare(M o1, M o2) {
        return o1.getMethod().compareTo(o2.getMethod());
      }
    })) {

      M t = cor.getLeft();
      M c = cor.getRight();

      log.debug(
          "method '" + (t != null ? t.method + "()" : (c != null ? c.method + "()" : "?")) + "': t=" + t + " c=" + c);

      if (t != null && t.isToBeGenerated()) {
        failedInnerGeneration = true;
      }
      if (t != null && c == null) {
        if (t.isGenerationComplete()) {
          cache.add(t); // adds the generated element to the cache.
          t.concludeGenerationMarkTag();
        }
      }
      if (t == null && c != null) {
        cache.remove(t); // removes the element from the cache.
      }
      if (t != null && c != null) {
        if (t.isGenerationComplete()) {
          cache.replace(t); // replaces the element on the cache.
          t.concludeGenerationMarkTag();
        }
      }

    }
    return failedInnerGeneration;
  }

  @Override
  public boolean concludeGenerationTree(final AbstractDAOTag cache, final DatabaseAdapter adapter) {
    return this.concludeGeneration(cache, adapter);
  }

  // Abstract methods

  public abstract ClassPackage getPackage();

  public abstract String getJavaClassName();

  // Helper classes

  private class MethodTagContainer<M extends AbstractMethodTag<M>> implements Iterable<M>, Serializable {

    private static final long serialVersionUID = 1L;

    private List<M> methods = new ArrayList<M>();

    public void add(final M m) {
      this.methods.add(m);
    }

    public void remove(final M m) {
      for (Iterator<M> it = this.methods.iterator(); it.hasNext();) {
        M current = it.next();
        if (current.getMethod().equals(m.getMethod())) {
          it.remove();
          return;
        }
      }
    }

    public void replace(final M m) {
      for (ListIterator<M> it = this.methods.listIterator(); it.hasNext();) {
        M current = it.next();
        if (current.getMethod().equals(m.getMethod())) {
          it.set(m);
          return;
        }
      }
    }

    @SuppressWarnings("unused")
    public void copyMethods(final MethodTagContainer<M> source) {
      for (M m : source) {
        M d = m.duplicate();
        this.methods.add(d);
      }
    }

    @Override
    public Iterator<M> iterator() {
      return this.methods.iterator();
    }

    public List<M> toList() {
      return this.methods;
    }

  }

}
