package org.hotrod.config;

import java.io.File;
import java.util.ArrayList;
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
import org.nocrala.tools.database.tartarus.core.CatalogSchema;

@XmlRootElement(name = "hotrod")
public class HotRodConfigTag extends AbstractHotRodConfigTag {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = LogManager.getLogger(HotRodConfigTag.class);

  // Properties

  private File f;

  private GeneratorsTag generatorsTag = null;
  private NameSolverTag nameSolverTag = new NameSolverTag();
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

  @XmlElement(name = "name-solver")
  public void setNameSolver(final NameSolverTag nameSolverTag) {
    this.nameSolverTag = nameSolverTag;
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

  public NameSolverTag getNameSolverTag() {
    return nameSolverTag;
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

  public void validate(final File basedir, final File parentDir, final File f, final DatabaseAdapter adapter,
      final CatalogSchema currentCS) throws InvalidConfigurationFileException, GeneratorNotFoundException {

    this.f = f;

    // Generators

    if (this.generatorsTag == null) {
      this.generatorsTag = new GeneratorsTag();
      this.generatorsTag.enableDiscover();
    }
    this.generatorsTag.validate(basedir, parentDir, adapter, currentCS);
    super.addChild(this.generatorsTag);

    // Name Solver

    this.nameSolverTag.validate(this);

    // Type Solver

    this.typeSolverTag.validate(this);

    // Converters

    this.convertersByName = new HashMap<String, ConverterTag>();

    for (ConverterTag c : this.converters) {
      c.validate();
      if (this.convertersByName.containsKey(c.getName())) {
        throw new InvalidConfigurationFileException(this,
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

  // Apply current catalog/schema to declared tables

  public void applyCurrentSchema(CatalogSchema currentCS) {
    this.getAllTables().stream().forEach(t -> t.applyCurrentSchema(currentCS));
    this.getAllViews().stream().forEach(t -> t.applyCurrentSchema(currentCS));
    this.getAllEnums().stream().forEach(t -> t.applyCurrentSchema(currentCS));
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
