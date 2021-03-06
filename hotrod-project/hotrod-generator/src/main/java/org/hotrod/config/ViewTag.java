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
import org.hotrod.utils.Compare;
import org.hotrodorm.hotrod.utils.SUtil;
import org.nocrala.tools.database.tartarus.core.DatabaseObject;
import org.nocrala.tools.database.tartarus.core.JdbcColumn;
import org.nocrala.tools.database.tartarus.core.JdbcTable;

@XmlRootElement(name = "view")
public class ViewTag extends AbstractEntityDAOTag {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = LogManager.getLogger(ViewTag.class);

  // Properties

  private String catalog = null;
  private String schema = null;
  private String name = null;

  private ObjectId id = null;

  private String javaClassName = null;
  private List<ColumnTag> columns = new ArrayList<ColumnTag>();

  private DaosTag daosTag;
  private HotRodFragmentConfigTag fragmentConfig;
  private ClassPackage fragmentPackage;

  // Constructor

  public ViewTag() {
    super("view");
  }

  // Duplicate

  public ViewTag duplicate() {
    ViewTag d = new ViewTag();

    d.copyCommon(this);

    d.catalog = this.catalog;
    d.schema = this.schema;
    d.name = this.name;
    d.id = this.id;

    d.javaClassName = this.javaClassName;
    d.columns = this.columns;

    d.daosTag = this.daosTag;
    d.fragmentConfig = this.fragmentConfig;
    d.fragmentPackage = this.fragmentPackage;

    return d;
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
    this.javaClassName = javaName;
  }

  @XmlElement
  public void setColumn(final ColumnTag c) {
    this.columns.add(c);
    super.addChild(c);
  }

  // Behavior

  public void validate(final DaosSpringMyBatisTag daosTag, final HotRodConfigTag config,
      final HotRodFragmentConfigTag fragmentConfig, final DatabaseAdapter adapter)
      throws InvalidConfigurationFileException {
    log.debug("validate");

    this.daosTag = daosTag;
    this.fragmentConfig = fragmentConfig;
    this.fragmentPackage = this.fragmentConfig != null && this.fragmentConfig.getFragmentPackage() != null
        ? this.fragmentConfig.getFragmentPackage()
        : null;

    // name

    if (SUtil.isEmpty(this.name)) {
      throw new InvalidConfigurationFileException(this, //
          "Attribute 'name' cannot be empty", //
          "Attribute 'name' of tag <" + super.getTagName() + "> cannot be empty. "
              + "Must specify a database view name.");
    }

    // catalog

    Id catalogId;
    try {
      catalogId = this.catalog == null ? null : Id.fromTypedSQL(this.catalog, adapter);
    } catch (InvalidIdentifierException e) {
      String msg = "Invalid catalog name '" + this.catalog + "' on tag <" + super.getTagName() + "> for the table '"
          + this.name + "': " + e.getMessage();
      throw new InvalidConfigurationFileException(this, msg, msg);
    }

    // schema

    Id schemaId;
    try {
      schemaId = this.schema == null ? null : Id.fromTypedSQL(this.schema, adapter);
    } catch (InvalidIdentifierException e) {
      String msg = "Invalid schema name '" + this.schema + "' on tag <" + super.getTagName() + "> for the table '"
          + this.name + "': " + e.getMessage();
      throw new InvalidConfigurationFileException(this, msg, msg);
    }

    // java-name

    if (this.javaClassName != null) {
      this.javaClassName = this.javaClassName.trim();
      if (SUtil.isEmpty(this.javaClassName)) {
        throw new InvalidConfigurationFileException(this, //
            "When specified, 'java-name' cannot be empty", //
            "Invalid 'java-name' attribute value of tag <" + super.getTagName() + "> for the view '" + this.name
                + "'. When specified, the value cannot be empty.");
      }
      if (!this.javaClassName.matches(Patterns.VALID_JAVA_CLASS)) {
        throw new InvalidConfigurationFileException(this, //
            "Invalid 'java-name' attribute value '" + this.javaClassName + "': must start with an upper case letter, "
                + "and continue with any combination of letters, digits, underscores, or dollar signs", //
            "Invalid 'java-name' attribute value '" + this.javaClassName + "' of tag <" + super.getTagName()
                + "> for the view '" + this.name
                + "'. When specified, the java-name must start with an upper case letter, "
                + "and continue with any combination of letters, digits, underscores, or dollar signs.");
      }
    } else {
      String replacedName = null;
      try {
        replacedName = config.getNameSolverTag().resolveName(this.name, Scope.VIEW);
        if (replacedName != null) {
          this.javaClassName = Id.fromTypedSQL(replacedName, adapter).getJavaClassName();
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
    } catch (InvalidIdentifierException e) {
      String msg = "Invalid view name '" + this.name + "': " + e.getMessage();
      throw new InvalidConfigurationFileException(this, msg, msg);
    }

    try {
      this.id = new ObjectId(catalogId, schemaId, nameId, adapter);
    } catch (InvalidIdentifierException e) {
      String msg = "Invalid view object name: " + e.getMessage();
      throw new InvalidConfigurationFileException(this, msg, msg);
    }

    // columns

    Set<ColumnTag> cols = new HashSet<ColumnTag>();
    for (ColumnTag c : this.columns) {
      c.validate(config, adapter);
      if (cols.contains(c)) {
        throw new InvalidConfigurationFileException(c, //
            "Multiple <" + new ColumnTag().getTagName() + "> tags with the same name", //
            "Multiple <" + new ColumnTag().getTagName() + "> tags with the same name on tag <" + super.getTagName()
                + "> for table '" + this.id.getRenderedSQLName() + "'. You cannot specify the same column name "
                + "multiple times on a same view.");
      }
      cols.add(c);
    }

    // sequences, queries, and selects

    super.validate(daosTag, config, fragmentConfig, adapter);

  }

  public void validateAgainstDatabase(final Metadata metadata) throws InvalidConfigurationFileException {

    JdbcTable v = metadata.findJdbcView(this.id.getCanonicalSQLName());
    if (v == null) {
      throw new InvalidConfigurationFileException(this, //
          "Could not find database view '" + this.id.getRenderedSQLName() + "'", //
          "Could not find database view '" + this.id.getRenderedSQLName()
              + "' as specified in the <view> tag of the configuration file. "
              + "\n\nPlease verify the specified database catalog and schema names are correct according to this database. "
              + "You can try leaving the catalog/schema values empty, so " + Constants.TOOL_NAME
              + " will list all available values.");
    }

    for (ColumnTag c : this.columns) {
      JdbcColumn jc = metadata.findJdbcColumn(v, c.getName());
      if (jc == null) {
        throw new InvalidConfigurationFileException(this, //
            "Could not find column '" + c.getName() + "' on view", //
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
    return this.daosTag.getDaoPackage(this.fragmentPackage);
  }

  @Override
  public String getJavaClassName() {
    return this.id.getJavaClassName();
  }

  // Merging logic

  @Override
  public boolean sameKey(final AbstractConfigurationTag fresh) {
    try {
      ViewTag f = (ViewTag) fresh;
      return this.id.equals(f.id);
    } catch (ClassCastException e) {
      return false;
    }
  }

  @Override
  public boolean copyNonKeyProperties(final AbstractConfigurationTag fresh) {
    try {
      ViewTag f = (ViewTag) fresh;
      boolean different = !same(fresh);

      this.javaClassName = f.javaClassName;
      this.columns = f.columns;
      this.daosTag = f.daosTag;
      this.fragmentConfig = f.fragmentConfig;
      this.fragmentPackage = f.fragmentPackage;

      return different;
    } catch (ClassCastException e) {
      return false;
    }
  }

  @Override
  public boolean same(final AbstractConfigurationTag fresh) {
    try {
      ViewTag f = (ViewTag) fresh;
      return //
      Compare.same(this.id, f.id) && //
          Compare.same(this.javaClassName, f.javaClassName) && //
          Compare.same(this.columns, f.columns) //
      ;
    } catch (ClassCastException e) {
      return false;
    }
  }

  // Simple Caption

  @Override
  public String getInternalCaption() {
    return this.getTagName() + ":" + this.id;
  }

}
