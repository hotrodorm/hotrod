package org.hotrod.config;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrodorm.hotrod.utils.SUtil;
import org.nocrala.tools.database.tartarus.core.JdbcTable;

@XmlRootElement(name = "facet")
public class FacetTag extends AbstractConfigurationTag {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = LogManager.getLogger(FacetTag.class);

  // Properties

  private String name = null;
  private List<TableTag> tables = new ArrayList<TableTag>();
  private List<ViewTag> views = new ArrayList<ViewTag>();
  private List<EnumTag> enums = new ArrayList<EnumTag>();
  private List<ExecutorTag> daos = new ArrayList<ExecutorTag>();

  // Constructor

  public FacetTag() {
    super("facet");
    log.debug("init");
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

  // Behavior

  public void validate(final HotRodConfigTag config, final DaosSpringMyBatisTag daosTag,
      final HotRodFragmentConfigTag fragmentConfig, final DatabaseAdapter adapter)
      throws InvalidConfigurationFileException {

    // name

    if (SUtil.isEmpty(this.name)) {
      throw new InvalidConfigurationFileException(this,
          "Attribute 'name' of tag <" + super.getTagName() + "> cannot be empty. " + "You must specify a facet name.");
    }

    // daos

    for (TableTag t : this.tables) {
      t.validate(daosTag, config, fragmentConfig, adapter);
    }

    for (ViewTag v : this.views) {
      v.validate(daosTag, config, fragmentConfig, adapter);
    }

    for (EnumTag e : this.enums) {
      e.validate(daosTag, config, fragmentConfig, adapter);
    }

    for (ExecutorTag dao : this.daos) {
      dao.validate(daosTag, config, fragmentConfig, adapter);
    }

  }

  // Merge

  public void mergeOther(final FacetTag other) {
    this.tables.addAll(other.tables);
    this.views.addAll(other.views);
    this.enums.addAll(other.enums);
    this.daos.addAll(other.daos);
  }

  public void mergeOther(final List<TableTag> tables, final List<ViewTag> views, final List<EnumTag> enums,
      final List<ExecutorTag> daos) {
    this.tables.addAll(tables);
    this.views.addAll(views);
    this.enums.addAll(enums);
    this.daos.addAll(daos);
  }

  public void includeTable(final JdbcTable t, final DaosSpringMyBatisTag daosTag, final HotRodConfigTag config,
      final DatabaseAdapter adapter) throws InvalidConfigurationFileException {
    TableTag tt = new TableTag(t, daosTag, null, config, adapter);
    this.tables.add(tt);
  }

  public void includeView(final JdbcTable t, final DaosSpringMyBatisTag daosTag, final HotRodConfigTag config,
      final DatabaseAdapter adapter) throws InvalidConfigurationFileException {
    ViewTag vt = new ViewTag(t, daosTag, null, config, adapter);
    this.views.add(vt);
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

  public List<ExecutorTag> getExecutors() {
    return daos;
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

  // Simple Caption

  @Override
  public String getInternalCaption() {
    return this.getTagName() + ":" + this.name;
  }

}
