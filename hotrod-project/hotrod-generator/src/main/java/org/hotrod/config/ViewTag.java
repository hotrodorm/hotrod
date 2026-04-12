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
import org.nocrala.tools.database.tartarus.core.JdbcColumn;
import org.nocrala.tools.database.tartarus.core.JdbcTable;

@XmlRootElement(name = "view")
public class ViewTag extends AbstractEntityDAOTag {

  // Constants

  private static final Logger log = Logger.getLogger(ViewTag.class.getName());

  // Properties

  private String catalog = null;
  private String schema = null;
  private String name = null;

  private ObjectId id = null;

  @Deprecated
  private String javaName = null;
  private String entity = null;

  private List<ColumnTag> columns = new ArrayList<ColumnTag>();

  private JDBCTag jdbcTag;
  private HotRodFragmentConfigTag fragmentConfig;
  private ClassPackage fragmentPackage;

  // Constructor for XML config

  public ViewTag() {
    super("view", true);
  }

  // Constructor for Discover

  public ViewTag(final JdbcTable t, final JDBCTag jdbcTag, final HotRodFragmentConfigTag fragmentConfig,
      final HotRodConfigTag config, final DatabaseAdapter adapter) throws InvalidConfigurationFileException {
    super("view", true);

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
      String msg = "Cannot generate discovered view with the name: catalog:" + SUtil.coalesce(t.getCatalog(), "N/A")
          + " shema:" + SUtil.coalesce(t.getSchema(), "N/A") + " object_name:" + t.getName() + " " + e.getMessage()
          + ". Use a <view> tag to specify a regular name.";
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
          "Could not resolve java class name for view '" + this.name + "': " + e.getMessage());
    } catch (InvalidIdentifierException e) {
      throw new InvalidConfigurationFileException(this,
          "Could not resolve java class name for view '" + this.name + "': " + e.getMessage());
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

  @XmlElement
  public void setColumn(final ColumnTag c) {
    this.columns.add(c);
  }

  // Behavior

  public void validate(final JDBCTag jdbcTag, final HotRodConfigTag config,
      final HotRodFragmentConfigTag fragmentConfig, final DatabaseAdapter adapter, final CatalogSchema currentCS,
      Feedback feedback) throws InvalidConfigurationFileException {

    log.fine("validate");

    this.jdbcTag = jdbcTag;
    this.fragmentConfig = fragmentConfig;
    this.fragmentPackage = this.fragmentConfig != null && this.fragmentConfig.getFragmentPackage() != null
        ? this.fragmentConfig.getFragmentPackage()
        : null;

    // name

    if (SUtil.isEmpty(this.name)) {
      throw new InvalidConfigurationFileException(this, "Attribute 'name' of tag <" + super.getTagName()
          + "> cannot be empty. " + "Must specify a database view name.");
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
              "Invalid 'entity' attribute value of tag <" + super.getTagName() + "> for the view '" + this.name
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
            "Invalid 'java-name' attribute value of the tag <" + super.getTagName() + "> for the view '" + this.name
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
        replacedName = config.getNameSolverTag().resolveName(canonicalName, Scope.VIEW);
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
    } catch (InvalidIdentifierException e) {
      String msg = "Invalid view name '" + this.name + "': " + e.getMessage();
      throw new InvalidConfigurationFileException(this, msg);
    }

    try {
      this.id = new ObjectId(catalogId, schemaId, nameId, adapter);
    } catch (InvalidIdentifierException e) {
      String msg = "Invalid view object name: " + e.getMessage();
      throw new InvalidConfigurationFileException(this, msg);
    }

    // columns

    Set<ColumnTag> cols = new HashSet<ColumnTag>();
    for (ColumnTag c : this.columns) {
      c.validate(config, adapter);
      if (cols.contains(c)) {
        throw new InvalidConfigurationFileException(c,
            "Multiple <" + new ColumnTag().getTagName() + "> tags with the same name on tag <" + super.getTagName()
                + "> for table '" + this.id.getRenderedSQLName() + "'. You cannot specify the same column name "
                + "multiple times on a same view.");
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

  public void validateAgainstDatabase(final Metadata metadata) throws InvalidConfigurationFileException {

    JdbcTable v = metadata.findJdbcView(this.id.getCanonicalSQLName());
    if (v == null) {
      throw new InvalidConfigurationFileException(this, "Could not find database view '" + this.id.getRenderedSQLName()
          + "' as specified in the <view> tag of the configuration file. "
          + "\n\nPlease verify the specified database catalog and schema names are correct according to this database. "
          + "You can try leaving the catalog/schema values empty, so " + Constants.TOOL_NAME
          + " will list all available values.");
    }

    for (ColumnTag c : this.columns) {
      JdbcColumn jc = metadata.findJdbcColumn(v, c.getTypedSQLName());
      if (jc == null) {
        throw new InvalidConfigurationFileException(this,
            "Could not find column '" + c.getName() + "' on database view '" + this.id.getRenderedSQLName()
                + "', as specified in the <column> tag of the configuration file. ");
      }
    }

    for (SelectMethodTag s : this.getSelects()) {
      s.validateAgainstDatabase(metadata);
    }

  }

  // Search

  public ColumnTag findColumnTag(final String columnName, final DatabaseAdapter adapter) {
    for (ColumnTag ct : this.columns) {
      if (ct.isName(columnName, adapter)) {
        return ct;
      }
    }
    return null;
  }

  // Getters

  public ObjectId getId() {
    return this.id;
  }

  public DatabaseObject getDatabaseObjectId() {
    String c = this.id.getCatalog() == null ? null : this.id.getCatalog().getCanonicalSQLName();
    String s = this.id.getSchema() == null ? null : this.id.getSchema().getCanonicalSQLName();
    String n = this.id.getObject().getCanonicalSQLName();
    return new DatabaseObject(c, s, n);
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
