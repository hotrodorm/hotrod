package org.hotrod.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hotrod.config.NameSolverNameTag.Scope;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.CouldNotResolveNameException;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.InvalidIdentifierException;
import org.hotrod.generator.Feedback;
import org.hotrod.identifiers.Id;
import org.hotrod.identifiers.ObjectId;
import org.hotrod.identifiers.TypedSQLName;
import org.hotrod.metadata.Metadata;
import org.hotrod.utils.ClassPackage;
import org.hotrod.utils.SUtil;
import org.nocrala.tools.database.tartarus.core.CatalogSchema;
import org.nocrala.tools.database.tartarus.core.DatabaseObject;
import org.nocrala.tools.database.tartarus.core.JdbcForeignKey;
import org.nocrala.tools.database.tartarus.core.JdbcKey;
import org.nocrala.tools.database.tartarus.core.JdbcKeyColumn;
import org.nocrala.tools.database.tartarus.core.JdbcTable;
import org.nocrala.tools.lang.collector.listcollector.ListCollector;

@XmlRootElement(name = "table")
public class TableTag extends AbstractEntityDAOTag {

  // Constants

  private static final Logger log = Logger.getLogger(TableTag.class.getName());

  // Properties

  private String catalog = null;
  private String schema = null;
  private String name = null;

  private String extendsTable = null;
  private String extendsCatalog = null;
  private String extendsSchema = null;

  private ObjectId id = null;
  private ObjectId extendsId = null;
  private TableTag extendsTag = null;
  private JdbcTable extendsJdbcTable = null;
  private JdbcForeignKey extendsFK = null;

  @Deprecated
  private String javaName = null;
  private String entity = null;

  private String columnSeam = null;

  private OptimisticLockingTag optimisticLocking = null;
  private List<ColumnTag> columns = new ArrayList<ColumnTag>();

  private JDBCTag jdbcTag;
  private HotRodFragmentConfigTag fragmentConfig;
  private ClassPackage fragmentPackage;

  // Constructor for XML config

  public TableTag() {
    super("table", true);
  }

  // Constructor for Discover

  public TableTag(final JdbcTable t, final JDBCTag jdbcTag, final HotRodFragmentConfigTag fragmentConfig,
      final HotRodConfigTag config, final DatabaseAdapter adapter) throws InvalidConfigurationFileException {
    super("table", true);

    this.catalog = t.getCatalog();
    this.schema = t.getSchema();
    this.name = t.getName();

    this.jdbcTag = jdbcTag;
    this.fragmentConfig = fragmentConfig;
    this.fragmentPackage = this.fragmentConfig != null && this.fragmentConfig.getFragmentPackage() != null
        ? this.fragmentConfig.getFragmentPackage()
        : null;
    this.entity = resolveJavaName(config, adapter);

    try {

      Id catalogId = t.getCatalog() == null ? null : Id.fromCanonicalSQL(t.getCatalog(), adapter);
      Id schemaId = t.getSchema() == null ? null : Id.fromCanonicalSQL(t.getSchema(), adapter);
      Id nameId = Id.fromCanonicalSQL(t.getName(), adapter);

      this.id = new ObjectId(catalogId, schemaId, nameId, adapter);
    } catch (InvalidIdentifierException e) {
      String msg = "Cannot generate discovered table with the name: catalog:" + SUtil.coalesce(t.getCatalog(), "N/A")
          + " shema:" + SUtil.coalesce(t.getSchema(), "N/A") + " object_name:" + t.getName() + " " + e.getMessage()
          + ". Use a <table> to specify a regular name.";
      throw new InvalidConfigurationFileException(this, msg);
    }
  }

  private String resolveJavaName(final HotRodConfigTag config, final DatabaseAdapter adapter)
      throws InvalidConfigurationFileException {
    String replacedName = null;
    try {
      replacedName = config.getNameSolverTag().resolveName(this.name, Scope.TABLE);
      log.fine("### this.name=" + this.name + " -> replacedName=" + replacedName);
      return replacedName == null ? null : Id.fromCanonicalSQL(replacedName, adapter).getJavaClassName();
    } catch (CouldNotResolveNameException e) {
      throw new InvalidConfigurationFileException(this,
          "Could not resolve java class name for table '" + this.name + "': " + e.getMessage());
    } catch (InvalidIdentifierException e) {
      throw new InvalidConfigurationFileException(this,
          "Could not resolve java class name for table '" + this.name + "': " + e.getMessage());
    }
  }

  // JAXB Setters

  @XmlAttribute
  public void setName(final String name) {
    this.name = name;
  }

  @XmlAttribute
  public void setCatalog(final String catalog) {
    this.catalog = catalog;
  }

  @XmlAttribute
  public void setSchema(final String schema) {
    this.schema = schema;
  }

  @XmlAttribute(name = "java-name")
  public void setJavaName(final String javaName) {
    this.javaName = javaName;
  }

  @XmlAttribute(name = "entity")
  public void setEntity(final String entity) {
    this.entity = entity;
  }

  @XmlAttribute(name = "column-seam")
  public void setColumnSeam(final String columnSeam) {
    this.columnSeam = columnSeam;
  }

  @XmlElement(name = "optimistic-locking")
  public void setOptimisticLocking(final OptimisticLockingTag optimisticLocking) {
    this.optimisticLocking = optimisticLocking;
  }

  @XmlElement
  public void setColumn(final ColumnTag c) {
    this.columns.add(c);
  }

  // Behavior

  public static final String ENTITY_PATTERN = "[A-Z][a-zA-Z0-9_]*+";

  public void validate(final JDBCTag jdbcTag, final HotRodConfigTag config,
      final HotRodFragmentConfigTag fragmentConfig, final DatabaseAdapter adapter, final CatalogSchema currentCS,
      Feedback feedback) throws InvalidConfigurationFileException {

    this.jdbcTag = jdbcTag;
    this.fragmentConfig = fragmentConfig;
    this.fragmentPackage = this.fragmentConfig != null && this.fragmentConfig.getFragmentPackage() != null
        ? this.fragmentConfig.getFragmentPackage()
        : null;

    // name

    if (SUtil.isEmpty(this.name)) {
      throw new InvalidConfigurationFileException(this, "Attribute 'name' of tag <" + super.getTagName()
          + "> cannot be empty. " + "Must specify a database table name.");
    }

    // catalog

    Id catalogId;
    try {
      catalogId = this.catalog == null ? null : Id.fromTypedSQL(this.catalog, adapter);
    } catch (InvalidIdentifierException e) {
      String msg = "Invalid catalog name '" + this.catalog + "' on tag <" + super.getTagName() + "> for the table '"
          + this.name + "': " + e.getMessage();
      throw new InvalidConfigurationFileException(this, msg);
    }

    // schema

    Id schemaId;
    try {
      schemaId = this.schema == null ? null : Id.fromTypedSQL(this.schema, adapter);
    } catch (InvalidIdentifierException e) {
      String msg = "Invalid schema name '" + this.schema + "' on tag <" + super.getTagName() + "> for the table '"
          + this.name + "': " + e.getMessage();
      throw new InvalidConfigurationFileException(this, msg);
    }

    // entity & java-name

    if (this.javaName == null) {
      if (this.entity != null) {
        if (!this.entity.matches(TableTag.ENTITY_PATTERN)) {
          throw new InvalidConfigurationFileException(this,
              "Invalid 'entity' attribute value of tag <" + super.getTagName() + "> for the table '" + this.name
                  + "'. When specified, the value must start with an upper case letter, "
                  + "and continue with any combination of letters, digits, and underscores, but found: " + this.entity);
        }
      }
    } else {
      if (this.entity != null) {
        throw new InvalidConfigurationFileException(this, "Either the attribute 'entity' or 'java-name' of the tag <"
            + super.getTagName() + "> can be specified at the same time. " + "Use the former when possible.");
      }
      if (!this.javaName.matches(TableTag.ENTITY_PATTERN)) {
        throw new InvalidConfigurationFileException(this,
            "Invalid 'java-name' attribute value of tag <" + super.getTagName() + "> for the table '" + this.name
                + "'. When specified, the value must start with an upper case letter, "
                + "and continue with any combination of letters, digits, and underscores, but found: " + this.javaName);
      }
      this.entity = this.javaName;
      this.javaName = null;
    }

    if (this.entity == null) {
      String replacedName = null;
      try {
        TypedSQLName natural = new TypedSQLName(this.name);
        String canonicalName = adapter.canonizeName(natural.getName(), natural.isQuoted());
        replacedName = config.getNameSolverTag().resolveName(canonicalName, Scope.TABLE);
//        log.info("### canonicalName=" + canonicalName + " -> replacedName=" + replacedName);
        if (replacedName != null) {
          this.entity = Id.fromCanonicalSQL(replacedName, adapter).getJavaClassName();
          log.fine(" done.");
        }
      } catch (CouldNotResolveNameException e) {
        throw new InvalidConfigurationFileException(this,
            "Could not resolve java class name for table '" + this.name + "': " + e.getMessage());
      } catch (InvalidIdentifierException e) {
        throw new InvalidConfigurationFileException(this,
            "Could not resolve java class name for table '" + this.name + "': " + e.getMessage());
      }
    }

    // Assemble object id

    Id nameId;
    try {
      nameId = this.entity == null ? Id.fromTypedSQL(this.name, adapter)
          : Id.fromTypedSQLAndJavaClass(this.name, adapter, this.entity);
      log.fine(">>> nameId=" + nameId.getCanonicalSQLName() + " / " + nameId.getJavaClassName());
    } catch (InvalidIdentifierException e) {
      String msg = "Invalid table name '" + this.name + "': " + e.getMessage();
      throw new InvalidConfigurationFileException(this, msg);
    }

    try {
      this.id = new ObjectId(catalogId, schemaId, nameId, adapter);
    } catch (InvalidIdentifierException e) {
      String msg = "Invalid table object name: " + e.getMessage();
      throw new InvalidConfigurationFileException(this, msg);
    }

    // column-seam: no validation necessary

    // implements: no validation necessary

    // optimistic-locking

    if (this.optimisticLocking != null) {
      this.optimisticLocking.validate();
    }

    // columns

    Set<ColumnTag> cols = new HashSet<ColumnTag>();
    for (ColumnTag c : this.columns) {
      c.validate(config, adapter);
      if (cols.contains(c)) {
        throw new InvalidConfigurationFileException(c,
            "Multiple <" + new ColumnTag().getTagName() + "> tags with the same name on tag <" + super.getTagName()
                + "> for table '" + this.id.getCanonicalSQLName() + "'. You cannot specify the same column name "
                + "multiple times on a same table.");
      }
      cols.add(c);
    }

    // sequences, queries, and selects

    super.validate(jdbcTag, config, fragmentConfig, adapter, feedback);

  }

  public void applyCurrentSchema(CatalogSchema currentCS) {
    if (this.catalog == null && this.schema == null) {
      this.catalog = currentCS.getCatalog();
      this.schema = currentCS.getSchema();
    }
  }

  public void validateExtendsAgainstAllTables(final List<TableTag> allTables, final List<EnumTag> allEnums)
      throws InvalidConfigurationFileException {
    log.fine("v1");
    try {
      validateAndLinkExtends(allTables, allEnums);
    } catch (ExtendedTableNotFoundException e) {
      throw new InvalidConfigurationFileException(this, //
          "Table '" + this.getId() + "' is extending table '" + this.getExtendsId()
              + "' that cannot be found in the configuration file(s).");
    } catch (ExtendedTableFoundAsEnumException e) {
      throw new InvalidConfigurationFileException(this, //
          "Table '" + this.getId() + "' cannot extend table '" + this.getExtendsId()
              + "' since the latter is declared as an <enum>.");
    }
  }

  public void validateExtendsInSelectedFacets(final List<TableTag> facetTables, final List<EnumTag> facetEnums,
      final Set<String> facetNames) throws InvalidConfigurationFileException {
    log.fine("v2");

    for (TableTag ft : facetTables) {
      log.fine("v2 - " + ft.getId());
    }

    try {
      validateAndLinkExtends(facetTables, facetEnums);
    } catch (ExtendedTableNotFoundException e) {
      throw new InvalidConfigurationFileException(this, //
          "Table '" + this.getId() + "' is extending table '" + this.getExtendsId()
              + "' that cannot be found in the selected facets ("
              + facetNames.stream().collect(ListCollector.joining(",")) + ") of the configuration file(s).");
    } catch (ExtendedTableFoundAsEnumException e) {
      throw new InvalidConfigurationFileException(this, //
          "Table '" + this.getId() + "' cannot extend table '" + this.getExtendsId()
              + "' since the latter is declared as an <enum>.");
    }
  }

  private void validateAndLinkExtends(final List<TableTag> tables, final List<EnumTag> enums)
      throws InvalidConfigurationFileException, ExtendedTableNotFoundException, ExtendedTableFoundAsEnumException {
    if (this.getExtendsId() != null) {
      log.fine("*** " + this.id + " -> " + this.getExtendsId() + " ***");

      if (this.getExtendsId().equals(this.getId())) {
        throw new InvalidConfigurationFileException(this, //
            "Table '" + this.id + "' cannot extend itself.");
      }

      for (TableTag ot : tables) {
        log.fine(
            " -> ot: " + ot.id + " -- t.getExtendsId().equals(ot.getId())=" + this.getExtendsId().equals(ot.getId()));
        if (this.getExtendsId().equals(ot.getId())) {
          this.extendsTag = ot;
          if (ot.getExtendsId() != null) {
            throw new InvalidConfigurationFileException(this, //
                Constants.TOOL_NAME + " only supports single-level inheritance for tables, but table '" + this.getId()
                    + "' is declared to extend table '" + this.getExtendsId()
                    + "' that itself is declared to extend table '" + ot.getExtendsId() + "'.");
          }
          return;
        }
      }

      for (EnumTag et : enums) {
        if (this.getExtendsId().equals(et.getId())) {
          throw new ExtendedTableFoundAsEnumException();
        }
      }

      throw new ExtendedTableNotFoundException();
    }
  }

  private static class ExtendedTableNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

  }

  private static class ExtendedTableFoundAsEnumException extends Exception {

    private static final long serialVersionUID = 1L;

  }

  public void validateAgainstDatabase(final Metadata metadata) throws InvalidConfigurationFileException {

    JdbcTable jt = metadata.findJdbcTable(this.id.getCanonicalSQLName());

    if (jt == null) {
      throw new InvalidConfigurationFileException(this, "Could not find database table '"
          + this.id.getCanonicalSQLName() + "' as specified in the <table> tag of the configuration file. "
          + "\n\nPlease verify the specified database catalog and schema names are correct according to this database. "
          + "You can try leaving the catalog/schema values empty, so " + Constants.TOOL_NAME
          + " will list all available values.");
    }

    for (ColumnTag ct : this.columns) {
      ct.populateJdbcElements(metadata, jt);
      if (ct.getJdbcColumn() == null) {
        throw new InvalidConfigurationFileException(ct,
            "Could not find column '" + ct.getName() + "' on database table '" + this.id.getCanonicalSQLName()
                + "', as specified in the <column> tag of the configuration file. ");
      }
    }

    // Multi-column: identity & sequence per column

    for (ColumnTag ct : this.columns) {
      ct.validateAgainstDatabase(metadata);
    }

    if (this.optimisticLocking != null) {
      this.optimisticLocking.validateAgainstDatabase(metadata, this.id.getCanonicalSQLName(), jt);
    }

    for (SelectMethodTag s : this.getSelects()) {
      s.validateAgainstDatabase(metadata);
    }

    // validate extends as an FK from the PK to the parent PK

    if (this.extendsTag != null) {
      this.extendsJdbcTable = metadata.findJdbcTable(this.extendsTag.id.getCanonicalSQLName());
      if (this.extendsJdbcTable == null) {
        throw new InvalidConfigurationFileException(this, //
            "Could not find database table '" + this.extendsTag.id + "'. "
                + "\n\nPlease verify the specified database catalog and schema names are correct according to this database.");
      }
      JdbcKey pk = jt.getPk();
      if (pk == null) {
        throw new InvalidConfigurationFileException(this, //
            "Could not find a primary key on table '" + this.id + "' that extends table '" + this.extendsTag.id + "'. "
                + "In order to extend another table this table must have a primary key. "
                + "Also this primary key must have a foreign key constraint against the parent table.");
      }
      if (pk.getKeyColumns().size() != 1) {
        throw new InvalidConfigurationFileException(this, //
            "Composite primary key found on table '" + this.id + "' that extends table '" + this.extendsTag.id + "'. "
                + "In order to extend another table this table must have a single-column primary key. "
                + "Also this primary key must have a foreign key constraint against the parent table.");
      }

      this.extendsFK = null;
      log.fine("* cpk: " + renderKey(pk));
      for (JdbcForeignKey fk : jt.getImportedFks()) {
        log.fine("* FK: " + renderKey(fk.getLocalKey()) + " -> " + renderKey(fk.getRemoteKey()));
        boolean fkFromPK = equivalentKeys(fk.getLocalKey(), pk);
        log.fine(" - cfk.getLocalKey().isEquivalentTo(cpk) = " + fkFromPK);
        if (fkFromPK) {
          if (this.extendsFK != null) {
            throw new InvalidConfigurationFileException(this, //
                "Multiple foreign keys found on table '" + this.id
                    + "' from its primary key column(s) to the the parent table '" + this.extendsId
                    + "'. In order to extend another table this table's primary key must have "
                    + "a single foreign key constraint from its primary key against the parent table.");
          }
          this.extendsFK = fk;
        }
      }
      if (this.extendsFK == null) {
        throw new InvalidConfigurationFileException(this, //
            "Could not find a foreign key on table '" + this.id
                + "' from its primary key column(s) against the the parent table '" + this.extendsId
                + "'. In order to extend another table this table's primary key must have "
                + "a single foreign key constraint from its primary key against the parent table.");
      }

    }

  }

  private boolean equivalentKeys(final JdbcKey a, final JdbcKey b) {
    if (a == null || b == null) {
      return false;
    }

    List<JdbcKeyColumn> ac = new ArrayList<>(a.getKeyColumns());
    ac.sort((x, y) -> Integer.compare(x.getColumnSequence(), y.getColumnSequence()));

    List<JdbcKeyColumn> bc = new ArrayList<>(b.getKeyColumns());
    bc.sort((x, y) -> Integer.compare(x.getColumnSequence(), y.getColumnSequence()));

    if (ac.size() != bc.size()) {
      return false;
    }
    for (int i = 0; i < ac.size(); i++) {
      if (!ac.get(i).getColumn().isEquivalentTo(bc.get(i).getColumn())) {
        return false;
      }
    }
    return true;
  }

  private String renderKey(final JdbcKey k) {
    return "("
        + k.getKeyColumns().stream().sorted((a, b) -> Integer.compare(a.getColumnSequence(), b.getColumnSequence()))
            .map(c -> c.getColumn().getName()).collect(ListCollector.joining(","))
        + ")";
  }

  // Search

  public ColumnTag findColumnTag(final String jdbcName, final DatabaseAdapter adapter) {
    for (ColumnTag ct : this.columns) {
      if (ct.isName(jdbcName, adapter)) {
        return ct;
      }
    }
    return null;
  }

  public String toString() {
    return this.catalog + "/" + this.schema + "." + this.name;
  }

  // Getters

  public String getColumnSeam() {
    return this.columnSeam;
  }

  public ObjectId getExtendsId() {
    return this.extendsId;
  }

  public ObjectId getId() {
    return this.id;
  }

  public TableTag getExtendsTag() {
    return this.extendsTag;
  }

  public JdbcTable getExtendsJdbcTable() {
    return this.extendsJdbcTable;
  }

  public DatabaseObject getDatabaseObject() {
    String c = this.id.getCatalog() == null ? null : this.id.getCatalog().getCanonicalSQLName();
    String s = this.id.getSchema() == null ? null : this.id.getSchema().getCanonicalSQLName();
    String n = this.id.getObject().getCanonicalSQLName();
    return new DatabaseObject(c, s, n);
  }

  public OptimisticLockingTag getOptimisticLocking() {
    return optimisticLocking;
  }

  public List<ColumnTag> getColumns() {
    return columns;
  }

  public HotRodFragmentConfigTag getFragmentConfig() {
    return fragmentConfig;
  }

  // DAO Tag implementation

  @Override
  public ClassPackage getPackage() {
    return this.jdbcTag.getDAOPackage(this.fragmentPackage);
  }

  @Override
  public String getJavaClassName() {
    return this.id.getJavaClassName();
  }

  // Simple Caption

  @Override
  public String getInternalCaption() {
    return this.getTagName() + ":" + this.id;
  }

}
