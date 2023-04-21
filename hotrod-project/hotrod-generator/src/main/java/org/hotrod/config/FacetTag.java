package org.hotrod.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrodorm.hotrod.utils.SUtil;
import org.nocrala.tools.database.tartarus.core.CatalogSchema;
import org.nocrala.tools.database.tartarus.core.DatabaseObjectId;
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

  private Set<DatabaseObjectId> tableIds = new HashSet<>();
  private Set<DatabaseObjectId> viewIds = new HashSet<>();
  private Set<DatabaseObjectId> enumIds = new HashSet<>();

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
    mergeTable(table);
  }

  @XmlElement
  public void setView(final ViewTag view) {
    mergeView(view);
  }

  @XmlElement
  public void setEnum(final EnumTag e) {
    mergeEnum(e);
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

  // Merging

  public void mergeOther(final FacetTag other) {
    other.tables.stream().forEach(t -> mergeTable(t));
    other.views.stream().forEach(t -> mergeView(t));
    other.enums.stream().forEach(t -> mergeEnum(t));
    this.daos.addAll(other.daos);
  }

  public void mergeOther(final List<TableTag> tables, final List<ViewTag> views, final List<EnumTag> enums,
      final List<ExecutorTag> daos) {
    tables.stream().forEach(t -> mergeTable(t));
    views.stream().forEach(t -> mergeView(t));
    enums.stream().forEach(t -> mergeEnum(t));
    this.daos.addAll(daos);
  }

  public void includeTable(final JdbcTable t, final DaosSpringMyBatisTag daosTag, final HotRodConfigTag config,
      final DatabaseAdapter adapter) throws InvalidConfigurationFileException {
    TableTag tt = new TableTag(t, daosTag, null, config, adapter);
    mergeTable(tt);
  }

  public void includeView(final JdbcTable t, final DaosSpringMyBatisTag daosTag, final HotRodConfigTag config,
      final DatabaseAdapter adapter) throws InvalidConfigurationFileException {
    ViewTag vt = new ViewTag(t, daosTag, null, config, adapter);
    mergeView(vt);
  }

  private void mergeTable(final TableTag table) {
    log.debug("merge: " + table.toString() + " stack: " + Stream.of(Thread.currentThread().getStackTrace()).skip(1).limit(20)
        .map(s -> s.getClassName() + "(" + s.getLineNumber() + ")").collect(Collectors.joining(", ")));
    if (table.getDatabaseObject().getName().equals("player")) {
      log.debug(">> Before: 'player' ids=" + this.tableIds.stream().filter(t -> t.getName().equals("player")).count() //
          + " table: " + table.toString() //
          + " found: "
          + this.tableIds.stream().filter(t -> t.getName().equals("player")).map(t -> t.toString())
              .collect(Collectors.joining(", ")) //
          + " tables=" + this.tables.stream().filter(t -> t.getDatabaseObject().getName().equals("player")).count() //
          + " contains=" + this.tableIds.contains(table.getDatabaseObject().getId()));
    }
    if (!this.tableIds.contains(table.getDatabaseObject().getId())) {
      this.tableIds.add(table.getDatabaseObject().getId());
      this.tables.add(table);
    }
    if (table.getDatabaseObject().getName().equals("player")) {
      log.debug(">> After: 'player' ids=" + this.tableIds.stream().filter(t -> t.getName().equals("player")).count()
          + " tables=" + this.tables.stream().filter(t -> t.getDatabaseObject().getName().equals("player")).count());
    }
  }

  private void mergeView(final ViewTag view) {
    if (!this.viewIds.contains(view.getDatabaseObjectId().getId())) {
      this.viewIds.add(view.getDatabaseObjectId().getId());
      this.views.add(view);
    }
  }

  private void mergeEnum(final EnumTag e) {
    if (!this.enumIds.contains(e.getDatabaseObjectId().getId())) {
      this.enumIds.add(e.getDatabaseObjectId().getId());
      this.enums.add(e);
    }
  }

  // Apply current CS

  public void applyCurrentCatalogSchema(final CatalogSchema cs) {

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
