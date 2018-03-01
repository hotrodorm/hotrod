package org.hotrod.config;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.runtime.util.SUtils;
import org.hotrod.utils.Compare;

@XmlRootElement(name = "facet")
public class FacetTag extends AbstractConfigurationTag {

  // Constants

  private static final Logger log = Logger.getLogger(FacetTag.class);

  // Properties

  private String name = null;
  private List<TableTag> tables = new ArrayList<TableTag>();
  private List<ViewTag> views = new ArrayList<ViewTag>();
  private List<EnumTag> enums = new ArrayList<EnumTag>();
  private List<ExecutorTag> daos = new ArrayList<ExecutorTag>();
  private List<SelectClassTag> selects = new ArrayList<SelectClassTag>();

  // Constructor

  public FacetTag() {
    super("facet");
  }

  // JAXB Setters

  @XmlAttribute
  public void setName(final String name) {
    this.name = name;
  }

  @XmlElement
  public void setTable(final TableTag table) {
    this.tables.add(table);
  }

  @XmlElement
  public void setView(final ViewTag view) {
    this.views.add(view);
  }

  @XmlElement
  public void setEnum(final EnumTag e) {
    this.enums.add(e);
  }

  @XmlElement
  public void setDao(final ExecutorTag dao) {
    this.daos.add(dao);
  }

  @XmlElement
  public void setSelect(final SelectClassTag select) {
    this.selects.add(select);
  }

  // Behavior

  public void validate(final HotRodConfigTag config, final DaosTag daosTag,
      final HotRodFragmentConfigTag fragmentConfig) throws InvalidConfigurationFileException {

    // name

    if (SUtils.isEmpty(this.name)) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(),
          "Attribute 'name' of tag <" + super.getTagName() + "> cannot be empty. " + "You must specify a facet name.");
    }

    // daos

    for (TableTag t : this.tables) {
      t.validate(daosTag, config, fragmentConfig);
    }

    for (ViewTag v : this.views) {
      v.validate(daosTag, config, fragmentConfig);
    }

    for (EnumTag e : this.enums) {
      e.validate(daosTag, fragmentConfig);
    }

    for (ExecutorTag dao : this.daos) {
      dao.validate(daosTag, config, fragmentConfig);
    }

    for (SelectClassTag s : this.selects) {
      s.validate(daosTag, config, fragmentConfig);
    }

  }

  // Merge

  public void mergeOther(final FacetTag other) {
    this.tables.addAll(other.tables);
    this.views.addAll(other.views);
    this.enums.addAll(other.enums);
    this.daos.addAll(other.daos);

    this.selects.addAll(other.selects);
    log.debug("----> SELECTS (facet: " + this.name + ")");
    for (SelectClassTag s : this.selects) {
      log.debug("----> select " + s.getJavaClassName());
    }
    log.debug("----> ---");
  }

  public void mergeOther(final List<TableTag> tables, final List<ViewTag> views, final List<EnumTag> enums,
      final List<ExecutorTag> daos, final List<SelectClassTag> selects) {
    this.tables.addAll(tables);
    this.views.addAll(views);
    this.enums.addAll(enums);
    this.daos.addAll(daos);
    this.selects.addAll(selects);
  }

  // Getters

  public String getName() {
    return name;
  }

  public List<TableTag> getTables() {
    return tables;
  }

  public List<ViewTag> getViews() {
    return views;
  }

  public List<EnumTag> getEnums() {
    return this.enums;
  }

  public List<ExecutorTag> getDaos() {
    return daos;
  }

  public List<SelectClassTag> getSelects() {
    return selects;
  }

  // ToString

  public String toString() {
    return this.name;
  }

  // Indexable methods

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    FacetTag other = (FacetTag) obj;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    return true;
  }

  // Merging logic

  @Override
  public boolean sameKey(final AbstractConfigurationTag fresh) {
    try {
      FacetTag f = (FacetTag) fresh;
      return this.name.equals(f.name);
    } catch (ClassCastException e) {
      return false;
    }
  }

  @Override
  public boolean copyNonKeyProperties(final AbstractConfigurationTag fresh) {
    try {
      FacetTag f = (FacetTag) fresh;
      boolean different = !same(fresh);

      this.tables = f.tables;
      this.views = f.views;
      this.enums = f.enums;
      this.daos = f.daos;
      this.selects = f.selects;

      return different;
    } catch (ClassCastException e) {
      return false;
    }
  }

  @Override
  public boolean same(final AbstractConfigurationTag fresh) {
    try {
      FacetTag f = (FacetTag) fresh;
      return //
      Compare.same(this.tables, f.tables) && //
          Compare.same(this.views, f.views) && //
          Compare.same(this.enums, f.enums) && //
          Compare.same(this.daos, f.daos) && //
          Compare.same(this.selects, f.selects);
    } catch (ClassCastException e) {
      return false;
    }
  }

}
