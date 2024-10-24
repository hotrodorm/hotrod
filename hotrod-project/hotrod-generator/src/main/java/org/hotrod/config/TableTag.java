package org.hotrod.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.config.NameSolverNameTag.Scope;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.CouldNotResolveNameException;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.InvalidIdentifierException;
import org.hotrod.identifiers.Id;
import org.hotrod.identifiers.ObjectId;
import org.hotrod.metadata.Metadata;
import org.hotrod.utils.ClassPackage;
import org.hotrodorm.hotrod.utils.SUtil;
import org.nocrala.tools.database.tartarus.core.CatalogSchema;
import org.nocrala.tools.database.tartarus.core.DatabaseObject;
import org.nocrala.tools.database.tartarus.core.JdbcForeignKey;
import org.nocrala.tools.database.tartarus.core.JdbcKey;
import org.nocrala.tools.database.tartarus.core.JdbcKeyColumn;
import org.nocrala.tools.database.tartarus.core.JdbcTable;
import org.nocrala.tools.lang.collector.listcollector.ListCollector;

@XmlRootElement(name = "table")
public class TableTag extends AbstractEntityDAOTag {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = LogManager.getLogger(TableTag.class);

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

  private String javaClassName = null;
  private String columnSeam = null;

  private ClassicFKNavigationTag classicFKNavigation = null;
  private VersionControlColumnTag versionControlColumn = null;
  private List<ColumnTag> columns = new ArrayList<ColumnTag>();

  private DaosTag daosTag;
  private HotRodFragmentConfigTag fragmentConfig;
  private ClassPackage fragmentPackage;

  // Constructor for XML config

  public TableTag() {
    super("table", true);
  }

  // Constructor for Discover

  public TableTag(final JdbcTable t, final DaosTag daosTag, final HotRodFragmentConfigTag fragmentConfig,
      final HotRodConfigTag config, final DatabaseAdapter adapter) throws InvalidConfigurationFileException {
    super("table", true);

    this.catalog = t.getCatalog();
    this.schema = t.getSchema();
    this.name = t.getName();

    this.daosTag = daosTag;
    this.fragmentConfig = fragmentConfig;
    this.fragmentPackage = this.fragmentConfig != null && this.fragmentConfig.getFragmentPackage() != null
        ? this.fragmentConfig.getFragmentPackage()
        : null;
    this.javaClassName = resolveJavaName(config, adapter);

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
      log.debug("### this.name=" + this.name + " -> replacedName=" + replacedName);
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

  @XmlAttribute(name = "extends")
  public void setExtendsTable(final String extendsTable) {
    this.extendsTable = extendsTable;
  }

  @XmlAttribute(name = "extends-catalog")
  public void setExtendsCatalog(final String extendsCatalog) {
    this.extendsCatalog = extendsCatalog;
  }

  @XmlAttribute(name = "extends-schema")
  public void setExtendsSchema(final String extendsSchema) {
    this.extendsSchema = extendsSchema;
  }

  @XmlAttribute(name = "java-name")
  public void setJavaName(final String javaName) {
    this.javaClassName = javaName;
  }

  @XmlAttribute(name = "column-seam")
  public void setColumnSeam(final String columnSeam) {
    this.columnSeam = columnSeam;
  }

  @XmlElement(name = "classic-fk-navigation")
  public void setClassicFKNavigation(final ClassicFKNavigationTag classicFKNavigation) {
    this.classicFKNavigation = classicFKNavigation;
  }

  @XmlElement(name = "version-control-column")
  public void setVersionControlColumn(final VersionControlColumnTag versionControlColumn) {
    this.versionControlColumn = versionControlColumn;
    super.addChild(versionControlColumn);
  }

  @XmlElement
  public void setColumn(final ColumnTag c) {
    this.columns.add(c);
    super.addChild(c);
  }

  // Behavior

  public void validate(final DaosTag daosTag, final HotRodConfigTag config,
      final HotRodFragmentConfigTag fragmentConfig, final DatabaseAdapter adapter, final CatalogSchema currentCS)
      throws InvalidConfigurationFileException {

    this.daosTag = daosTag;
    this.fragmentConfig = fragmentConfig;
    this.fragmentPackage = this.fragmentConfig != null && this.fragmentConfig.getFragmentPackage() != null
        ? this.fragmentConfig.getFragmentPackage()
        : null;

    // name

    if (SUtil.isEmpty(this.name)) {
      throw new InvalidConfigurationFileException(this, "Attribute 'name' of tag <" + super.getTagName()
          + "> cannot be empty. " + "Must specify a database table name.");
    }

//    if (this.catalog == null && this.schema == null) {
//      this.applyCurrentSchema(currentCS);
//    }

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

    // extends

    if (this.extendsTable == null) {

      if (this.extendsCatalog != null) {
        throw new InvalidConfigurationFileException(this,
            "Attribute 'extends-catalog' cannot only specified if the 'extends' attribute is present");
      }
      if (this.extendsSchema != null) {
        throw new InvalidConfigurationFileException(this,
            "Attribute 'extends-schema' cannot only specified if the 'extends' attribute is present");
      }
      this.extendsId = null;

    } else {

      // extends

      Id extendsTableId;
      try {
        extendsTableId = Id.fromTypedSQL(this.extendsTable, adapter);
      } catch (InvalidIdentifierException e) {
        String msg = "Invalid value '" + this.extendsTable + "' on 'extends' attribute: " + e.getMessage();
        throw new InvalidConfigurationFileException(this, msg);
      }

      // extends-catalog

      Id extendsCatalogId;
      try {
        extendsCatalogId = this.extendsCatalog == null ? null : Id.fromTypedSQL(this.extendsCatalog, adapter);
      } catch (InvalidIdentifierException e) {
        String msg = "Invalid extends-catalog name '" + this.catalog + "' on tag <" + super.getTagName()
            + "> for the table '" + this.name + "': " + e.getMessage();
        throw new InvalidConfigurationFileException(this, msg);
      }

      // extends-schema

      Id extendsSchemaId;
      try {
        extendsSchemaId = this.extendsSchema == null ? null : Id.fromTypedSQL(this.extendsSchema, adapter);
      } catch (InvalidIdentifierException e) {
        String msg = "Invalid extends-schema name '" + this.schema + "' on tag <" + super.getTagName()
            + "> for the table '" + this.name + "': " + e.getMessage();
        throw new InvalidConfigurationFileException(this, msg);
      }

      // Assemble extendsId

      try {
        this.extendsId = new ObjectId(extendsCatalogId, extendsSchemaId, extendsTableId, adapter);
      } catch (InvalidIdentifierException e) {
        String msg = "Invalid 'extends' table object name: " + e.getMessage();
        throw new InvalidConfigurationFileException(this, msg);
      }

    }

    // java-name

    if (this.javaClassName != null) {
      this.javaClassName = this.javaClassName.trim();
      if (SUtil.isEmpty(this.javaClassName)) {
        throw new InvalidConfigurationFileException(this, "Invalid 'java-name' attribute value of tag <"
            + super.getTagName() + "> for the table '" + this.name + "'. When specified, the value cannot be empty.");
      }
      if (!this.javaClassName.matches(Patterns.VALID_JAVA_CLASS)) {
        throw new InvalidConfigurationFileException(this,
            "Invalid 'java-name' attribute value '" + this.javaClassName + "' of tag <" + super.getTagName()
                + ">. When specified, the java-name must start with an upper case letter, "
                + "and continue with any combination of letters, digits, underscores, or dollar signs.");
      }
    } else {
      String replacedName = null;
      try {
        replacedName = config.getNameSolverTag().resolveName(this.name, Scope.TABLE);
        log.debug("### this.name=" + this.name + " -> replacedName=" + replacedName);
        if (replacedName != null) {
          this.javaClassName = Id.fromCanonicalSQL(replacedName, adapter).getJavaClassName();
          log.debug(" done.");
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
      nameId = this.javaClassName == null ? Id.fromTypedSQL(this.name, adapter)
          : Id.fromTypedSQLAndJavaClass(this.name, adapter, this.javaClassName);
      log.debug(">>> nameId=" + nameId.getCanonicalSQLName() + " / " + nameId.getJavaClassName());
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

    // version-control

    if (this.versionControlColumn != null) {
      this.versionControlColumn.validate();
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

    super.validate(daosTag, config, fragmentConfig, adapter);

  }

  public void applyCurrentSchema(CatalogSchema currentCS) {
    if (this.catalog == null && this.schema == null) {
      this.catalog = currentCS.getCatalog();
      this.schema = currentCS.getSchema();
    }
  }

  public void validateExtendsAgainstAllTables(final List<TableTag> allTables, final List<EnumTag> allEnums)
      throws InvalidConfigurationFileException {
    log.debug("v1");
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
    log.debug("v2");

    for (TableTag ft : facetTables) {
      log.debug("v2 - " + ft.getId());
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
      log.debug("*** " + this.id + " -> " + this.getExtendsId() + " ***");

      if (this.getExtendsId().equals(this.getId())) {
        throw new InvalidConfigurationFileException(this, //
            "Table '" + this.id + "' cannot extend itself.");
      }

      for (TableTag ot : tables) {
        log.debug(
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

    if (this.versionControlColumn != null) {
      this.versionControlColumn.validateAgainstDatabase(metadata, this.id.getCanonicalSQLName(), jt);
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
      log.debug("* cpk: " + renderKey(pk));
      for (JdbcForeignKey fk : jt.getImportedFks()) {
        log.debug("* FK: " + renderKey(fk.getLocalKey()) + " -> " + renderKey(fk.getRemoteKey()));
        boolean fkFromPK = equivalentKeys(fk.getLocalKey(), pk);
        log.debug(" - cfk.getLocalKey().isEquivalentTo(cpk) = " + fkFromPK);
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

  public ClassicFKNavigationTag getClassicFKNavigation() {
    return classicFKNavigation;
  }

  public VersionControlColumnTag getVersionControlColumn() {
    return versionControlColumn;
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
    return this.daosTag.getDaoPackage(this.fragmentPackage);
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
