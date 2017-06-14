package org.hotrod.config;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.utils.SUtils;

public class FacetTag {

  private static final Logger log = Logger.getLogger(FacetTag.class);

  static final String TAG_NAME = "facet";

  private String name = null;
  private List<TableTag> tables = new ArrayList<TableTag>();
  private List<ViewTag> views = new ArrayList<ViewTag>();
  private List<CustomDAOTag> daos = new ArrayList<CustomDAOTag>();
  private List<SelectTag> selects = new ArrayList<SelectTag>();

  public void validate(final HotRodConfigTag config, final DaosTag daosTag) throws InvalidConfigurationFileException {

    // name

    if (SUtils.isEmpty(this.name)) {
      throw new InvalidConfigurationFileException(
          "Attribute 'name' of tag <" + TAG_NAME + "> cannot be empty. " + "You must specify a facet name.");
    }

    // daos

    for (TableTag t : this.tables) {
      t.validate(daosTag, config);
    }

    for (ViewTag v : this.views) {
      v.validate(daosTag, config);
    }

    for (CustomDAOTag dao : this.daos) {
      dao.validate(daosTag);
    }

    for (SelectTag s : this.selects) {
      s.validate(daosTag, config);
    }

  }

  // Merge

  public void mergeOther(final FacetTag other) {
    this.tables.addAll(other.tables);
    this.views.addAll(other.views);
    this.daos.addAll(other.daos);

    this.selects.addAll(other.selects);
    log.debug("----> SELECTS (facet: " + this.name + ")");
    for (SelectTag s : this.selects) {
      log.debug("----> select " + s.getJavaClassName());
    }
    log.debug("----> ---");
  }

  public void mergeOther(final List<TableTag> tables, final List<ViewTag> views, final List<CustomDAOTag> daos,
      final List<SelectTag> selects) {
    this.tables.addAll(tables);
    this.views.addAll(views);
    this.daos.addAll(daos);
    this.selects.addAll(selects);
  }

  // Setters (digester)

  public void setName(final String name) {
    this.name = name;
  }

  public void addTable(final TableTag table) {
    this.tables.add(table);
  }

  public void addView(final ViewTag view) {
    this.views.add(view);
  }

  public void addDAO(final CustomDAOTag dao) {
    this.daos.add(dao);
  }

  public void addSelect(final SelectTag select) {
    this.selects.add(select);
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

  public List<CustomDAOTag> getDaos() {
    return daos;
  }

  public List<SelectTag> getSelects() {
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

}
