package org.hotrod.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import org.apache.log4j.Logger;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.utils.ClassPackage;

/**
 * <pre>
 *
 *     AbstractConfigurationTag <-----------------------------------------+
 *      ^                                                 ^               |
 *      |                                                 |               |
 *     AbstractDAOTag <---------------............        |               |
 *      ^          ^        ^        ^           .        |               |
 *      |          |        |        |           .        |               |
 * CustomDAOTag  TableTag  ViewTag  EnumTag  SelectTag  QueryMethodTag  SelectMethodTag
 * 
 * </pre>
 */

public abstract class AbstractDAOTag extends AbstractConfigurationTag {

  // Constants

  private static final Logger log = Logger.getLogger(AbstractDAOTag.class);

  // Properties

  private List<SequenceMethodTag> sequences = new ArrayList<SequenceMethodTag>();
  private List<QueryMethodTag> queries = new ArrayList<QueryMethodTag>();
  private List<SelectMethodTag> selects = new ArrayList<SelectMethodTag>();

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

  // Behavior

  protected void validate(final DaosTag daosTag, final HotRodConfigTag config,
      final HotRodFragmentConfigTag fragmentConfig) throws InvalidConfigurationFileException {

    // sequences

    Set<String> seqNames = new HashSet<String>();

    for (SequenceMethodTag s : this.sequences) {
      s.validate();
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

      if (methodNames.contains(s.getMethod())) {
        throw new InvalidConfigurationFileException(s.getSourceLocation(),
            "Duplicate method name '" + s.getMethod() + "' on <" + s.getTagName() + "> tag.");
      }
      methodNames.add(s.getMethod());

    }

  }

  // Getters

  public final List<SequenceMethodTag> getSequences() {
    return sequences;
  }

  public final List<QueryMethodTag> getQueries() {
    return queries;
  }

  public List<SelectMethodTag> getSelects() {
    return selects;
  }

  public final Set<String> getDeclaredMethodNames() {
    log.debug("get methods.");
    return this.declaredMethodNames;
  }

  // Abstract methods

  public abstract ClassPackage getPackage();

  public abstract String getJavaClassName();

}
