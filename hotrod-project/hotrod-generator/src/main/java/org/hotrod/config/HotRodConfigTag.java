package org.hotrod.config;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.GeneratorNotFoundException;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.utils.Compare;
import org.hotrod.utils.Correlator;
import org.hotrod.utils.Correlator.CorrelatedEntry;

@XmlRootElement(name = "hotrod")
public class HotRodConfigTag extends AbstractHotRodConfigTag {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = LogManager.getLogger(HotRodConfigTag.class);

  // Properties

  @SuppressWarnings("unused")
  private File f;

  private GeneratorsTag generatorsTag = null;
  private TypeSolverTag typeSolverTag = new TypeSolverTag(); // default that register columns

  private List<ConverterTag> converters = new ArrayList<ConverterTag>();
  private Map<String, ConverterTag> convertersByName = null;

  // Constructors

  public HotRodConfigTag() {
    super("hotrod");
    log.debug("init");
  }

  // JAXB Setters

  @XmlElement
  public void setGenerators(final GeneratorsTag generators) {
    this.generatorsTag = generators;
  }

  @XmlElement(name = "type-solver")
  public void setTypeSolver(final TypeSolverTag typeSolverTag) {
    this.typeSolverTag = typeSolverTag;
  }

  @XmlElement
  public void setConverter(final ConverterTag converter) {
    this.converters.add(converter);
  }

  // Getters

  public GeneratorsTag getGenerators() {
    return generatorsTag;
  }

  public TypeSolverTag getTypeSolverTag() {
    return typeSolverTag;
  }

  public List<ConverterTag> getConverters() {
    return this.converters;
  }

  public ConverterTag getConverterTagByName(final String name) {
    return this.convertersByName.get(name);
  }

  public void validate(final File basedir, final File parentDir, final File f, final String generatorName)
      throws InvalidConfigurationFileException, GeneratorNotFoundException {

    this.f = f;

    // Generators

    this.generatorsTag.validate(basedir, parentDir, generatorName);
    super.addChild(this.generatorsTag);

    // Type Solver

    if (this.typeSolverTag != null) {
      this.typeSolverTag.validate(this);
    }

    // Converters

    this.convertersByName = new HashMap<String, ConverterTag>();

    for (ConverterTag c : this.converters) {
      c.validate();
      if (this.convertersByName.containsKey(c.getName())) {
        throw new InvalidConfigurationFileException(this, //
            "Multiple converters with name '" + c.getName() + "'", //
            "Duplicate converter name. There are multiple <converter> tags with the same name: '" + c.getName() + "'.");
      }
      this.convertersByName.put(c.getName(), c);
    }

  }

  public void addConverterTags() {
    for (ConverterTag c : this.converters) {
      super.addChild(c);
    }
  }

  // // Entity Resolver
  //
  // public static class HotRodInternalEntityResolver implements EntityResolver
  // {
  //
  // private static final Logger log =
  // Logger.getLogger(HotRodInternalEntityResolver.class);
  //
  // private static final String DTD = "/hotrod.dtd";
  //
  // @Override
  // public InputSource resolveEntity(final String publicId, final String
  // systemId) throws SAXException, IOException {
  // log.debug("*** public: " + publicId + " system: " + systemId);
  // InputStream is = getClass().getResourceAsStream(DTD);
  // log.debug("*** is=" + is);
  // if (is == null) {
  // throw new IOException("Could not find DTD definition file/resource: " +
  // DTD);
  // }
  // return new InputSource(is);
  // }
  //
  // }

  // Merging logic

  @Override
  public boolean sameKey(final AbstractConfigurationTag fresh) {
    return super.commonSameKey(fresh) && true;
  }

  @Override
  public boolean copyNonKeyProperties(final AbstractConfigurationTag fresh) {
    log.debug("copying... " + this.getInternalCaption() + " fresh=" + fresh);
    try {
      HotRodConfigTag f = (HotRodConfigTag) fresh;
      boolean different = !super.commonSame(fresh) || !same(fresh);

      super.commonCopyNonKeyProperties(fresh);
      this.generatorsTag = f.generatorsTag;
      this.converters = f.converters;
      this.convertersByName = f.convertersByName;

      return different;
    } catch (ClassCastException e) {
      return false;
    }
  }

  @Override
  public boolean same(final AbstractConfigurationTag fresh) {
    try {
      HotRodConfigTag f = (HotRodConfigTag) fresh;
      return //
      super.commonSame(fresh) && //
          Compare.same(this.generatorsTag, f.generatorsTag) && //
          Compare.same(this.converters, f.converters) && //
          Compare.same(this.convertersByName, f.convertersByName) //
      ;
    } catch (ClassCastException e) {
      return false;
    }
  }

  // Update generated cache

  @Override
  public boolean concludeGeneration(final AbstractHotRodConfigTag unitCache, final DatabaseAdapter adapter) {
    log.debug("CONCLUDE 1");

    HotRodConfigTag cache = (HotRodConfigTag) unitCache;

    boolean successfulCommonGeneration = super.commonConcludeGeneration(cache, adapter);

    log.debug("CONCLUDE: successfulCommonGeneration=" + successfulCommonGeneration);

    boolean failedExtendedGeneration = false;

    // Converters

    log.debug("conclude 2 - converters");

    for (ConverterTag c : this.getConverters()) {
      if (!c.concludeGenerationMarkTag()) {
        failedExtendedGeneration = true;
      }
    }

    for (CorrelatedEntry<ConverterTag> cor : Correlator.correlate(this.getConverters(), cache.getConverters(),
        new Comparator<ConverterTag>() {
          @Override
          public int compare(ConverterTag o1, ConverterTag o2) {
            return adapter.canonizeName(o1.getName(), false).compareTo(adapter.canonizeName(o2.getName(), false));
          }
        })) {

      ConverterTag t = cor.getLeft();
      ConverterTag c = cor.getRight();

      if (t != null && t.isToBeGenerated()) {
        failedExtendedGeneration = true;
      }
      if (t != null && c == null) {
        if (t.isGenerationComplete()) {
          cache.add(t); // adds the generated element to the cache.
        }
      }
      if (t == null && c != null) {
        cache.remove(t, adapter); // removes the element from the cache.
      }
      if (t != null && c != null) {
        if (t.isGenerationComplete()) {
          cache.replace(t, adapter); // replaces the element on the cache.
        }
      }

    }

    // Generators (settings)

    this.generatorsTag.concludeGenerationMarkTag();

    // HotRodConfigTag

    log.debug("conclude 3 - end");
    log.debug("CONCLUDE: successfulCommonGeneration=" + successfulCommonGeneration + " failedExtendedGeneration="
        + failedExtendedGeneration);

    if (successfulCommonGeneration && !failedExtendedGeneration) {
      log.debug("conclude 4 - conclude");
      return this.concludeGenerationMarkTag();
    }

    return false;
  }

  // Setters

  protected void add(final ConverterTag c) {
    this.converters.add(c);
  }

  protected void remove(final ConverterTag c, final DatabaseAdapter adapter) {
    for (Iterator<ConverterTag> it = this.converters.iterator(); it.hasNext();) {
      ConverterTag current = it.next();
      if (adapter.equalConfigNames(current.getName(), c.getName())) {
        it.remove();
        return;
      }
    }
  }

  protected void replace(final ConverterTag c, final DatabaseAdapter adapter) {
    for (ListIterator<ConverterTag> it = this.converters.listIterator(); it.hasNext();) {
      ConverterTag current = it.next();
      if (adapter.equalConfigNames(current.getName(), c.getName())) {
        it.set(c);
        return;
      }
    }
  }

  // Metadata

  public File getFile() {
    return this.f;
  }

  // Simple Caption

  @Override
  public String getInternalCaption() {
    return this.getTagName();
  }

}
