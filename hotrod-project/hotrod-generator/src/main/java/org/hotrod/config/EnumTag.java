package org.hotrod.config;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.config.NameSolverNameTag.Scope;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.database.PropertyType;
import org.hotrod.exceptions.CouldNotResolveNameException;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.InvalidIdentifierException;
import org.hotrod.exceptions.UnresolvableDataTypeException;
import org.hotrod.identifiers.Id;
import org.hotrod.identifiers.ObjectId;
import org.hotrod.metadata.ColumnMetadata;
import org.hotrod.metadata.Metadata;
import org.hotrod.utils.ClassPackage;
import org.hotrod.utils.ValueTypeFactory;
import org.hotrod.utils.ValueTypeFactory.ValueTypeManager;
import org.hotrodorm.hotrod.utils.SUtil;
import org.nocrala.tools.database.tartarus.core.CatalogSchema;
import org.nocrala.tools.database.tartarus.core.DatabaseObject;
import org.nocrala.tools.database.tartarus.core.JdbcColumn;
import org.nocrala.tools.database.tartarus.core.JdbcDatabase;
import org.nocrala.tools.database.tartarus.core.JdbcTable;
import org.nocrala.tools.lang.collector.listcollector.ListWriter;

@XmlRootElement(name = "enum")
public class EnumTag extends AbstractEntityDAOTag {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = LogManager.getLogger(EnumTag.class);

  private static final String CLASS_NAME_PATTERN = "[A-Z][a-zA-Z0-9_]*";

  // Properties

  private String catalog = null;
  private String schema = null;
  private String name = null;

  private ObjectId id = null;

  private String javaClassName = null;
  private String nameCol = null;
  private List<NonPersistentTag> nonPersistents = new ArrayList<NonPersistentTag>();

  private JdbcTable table = null;
  private EnumColumn valueColumn = null;
  private EnumColumn nameColumn = null;

  private List<EnumConstant> tableConstants = new ArrayList<EnumConstant>();
  private List<EnumConstant> npConstants = new ArrayList<EnumConstant>();

  private LinkedHashMap<JdbcColumn, EnumColumn> extraColumns = null;

  private DaosTag daosTag;
  private HotRodConfigTag config;
  private HotRodFragmentConfigTag fragmentConfig = null;
  private ClassPackage fragmentPackage;

  // Constructor

  public EnumTag() {
    super("enum", false);
    log.debug("init");
  }

  // JAXB Setters

  @XmlAttribute
  public void setName(final String column) {
    this.name = column;
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
  public void setJavaClass(final String javaClass) {
    this.javaClassName = javaClass;
  }

  @XmlAttribute(name = "name-column")
  public void setNameCol(final String nameCol) {
    this.nameCol = nameCol;
  }

  @XmlElement(name = "non-persistent")
  public void setNonPersistent(final NonPersistentTag nonPersistent) {
    this.nonPersistents.add(nonPersistent);
  }

  // Behavior

  public void validate(final DaosTag daosTag, final HotRodConfigTag config,
      final HotRodFragmentConfigTag fragmentConfig, final DatabaseAdapter adapter, final CatalogSchema currentCS)
      throws InvalidConfigurationFileException {

    this.daosTag = daosTag;
    this.config = config;
    this.fragmentConfig = fragmentConfig;
    this.fragmentPackage = this.fragmentConfig != null && this.fragmentConfig.getFragmentPackage() != null
        ? this.fragmentConfig.getFragmentPackage()
        : null;

    // name

    if (SUtil.isEmpty(this.name)) {
      throw new InvalidConfigurationFileException(this, "Attribute 'name' of tag <" + super.getTagName()
          + "> cannot be empty. " + "Must specify the name of a database table.");
    }

    if (this.catalog == null && this.schema == null) {
      this.applyCurrentSchema(currentCS);
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

    // java-name

    if (!SUtil.isEmpty(this.javaClassName)) {
      if (!this.javaClassName.matches(CLASS_NAME_PATTERN)) {
        throw new InvalidConfigurationFileException(this,
            "The attribute 'java-name' of tag <" + super.getTagName() + "> with value '" + this.javaClassName
                + "' is not a valid Java class name. " + "When specified, it must start with an upper case letter, "
                + "and continue with letters, digits, and/or underscores.");
      }
    } else {
      String replacedName = null;
      try {
        replacedName = config.getNameSolverTag().resolveName(this.name, Scope.TABLE);
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
      String msg = "Invalid table name '" + this.name + "': " + e.getMessage();
      throw new InvalidConfigurationFileException(this, msg);
    }

    try {
      this.id = new ObjectId(catalogId, schemaId, nameId, adapter);
    } catch (InvalidIdentifierException e) {
      String msg = "Invalid table object name: " + e.getMessage();
      throw new InvalidConfigurationFileException(this, msg);
    }

    // name-column

    if (SUtil.isEmpty(this.nameCol)) {
      throw new InvalidConfigurationFileException(this,
          "Attribute 'name-column' of tag <" + super.getTagName() + "> cannot be empty. "
              + "Must specify the column name of the table that will be used as the name for each value for the enum.");
    }

    // non-persistent

    for (NonPersistentTag np : this.nonPersistents) {
      np.validate();
    }

  }

  public void applyCurrentSchema(CatalogSchema currentCS) {
    if (this.catalog == null && this.schema == null) {
      this.catalog = currentCS.getCatalog();
      this.schema = currentCS.getSchema();
    }
  }

  private static class EnumColumn implements Serializable {

    private static final long serialVersionUID = 1L;

    private JdbcColumn column;
    private Id id;
    private ValueTypeManager<?> valueTypeManager;

    public EnumColumn(final JdbcColumn column, final ValueTypeManager<?> valueTypeManager,
        final DatabaseAdapter adapter) throws InvalidIdentifierException {
      this.column = column;
      this.id = Id.fromCanonicalSQL(this.column.getName(), adapter);
      this.valueTypeManager = valueTypeManager;
    }

    public ValueTypeManager<?> getValueTypeManager() {
      return valueTypeManager;
    }

    public EnumProperty getProperty() {
      return new EnumProperty(this.valueTypeManager.getValueClassName(), this.id.getJavaMemberName());
    }

    public Id getId() {
      return id;
    }

  }

  public void validateAgainstDatabase(final Metadata metadata, final Connection conn, final DatabaseAdapter adapter)
      throws InvalidConfigurationFileException {

    // Validate the table existence

    this.table = metadata.findJdbcTable(this.id.getCanonicalSQLName());
    if (this.table == null) {
      throw new InvalidConfigurationFileException(this,
          "Could not find database table '" + this.id.getRenderedSQLName() + "'");
    }

    // Get the list of columns

    this.extraColumns = new LinkedHashMap<JdbcColumn, EnumColumn>();

    for (JdbcColumn c : this.table.getColumns()) {
      ValueTypeManager<?> m = resolveValueTypeManager(c, metadata.getAdapter());
      try {
        this.extraColumns.put(c, new EnumColumn(c, m, adapter));
      } catch (InvalidIdentifierException e) {
        String msg = "Invalid column name '" + c.getName() + "' on table '" + this.table.getName()
            + "'. Could not produce a valid identifier for it.";
        throw new InvalidConfigurationFileException(this, msg);
      }
    }

    // Validate the single-column PK

    if (this.table.getPk() == null) {
      throw new InvalidConfigurationFileException(this,
          "Enum table '" + this.id.getRenderedSQLName() + "' does not have a primary key. "
              + "A primary key is required on a table that is being used in an <" + super.getTagName() + "> tag.");
    }
    if (this.table.getPk().getKeyColumns().size() != 1) {
      throw new InvalidConfigurationFileException(this,
          "Enum table '" + this.id.getRenderedSQLName() + "' should not have a composite primary key. "
              + "A single-column primary key is required on a table that is being used in an <" + super.getTagName()
              + "> tag, but this one has " + this.table.getPk().getKeyColumns().size()
              + " columns in the primary key.");
    }

    JdbcColumn pkCol = this.table.getPk().getKeyColumns().get(0).getColumn();
    this.valueColumn = this.extraColumns.get(pkCol);
    this.extraColumns.remove(pkCol);

    // Validate the name column

    JdbcColumn nameCol = metadata.findJdbcColumn(this.table, this.nameCol);
    if (nameCol == null) {
      throw new InvalidConfigurationFileException(this,
          "Could not find column '" + this.nameCol + "' on table '" + this.id.getRenderedSQLName()
              + "' as specified in the attribute 'name-column' of the tag <" + super.getTagName() + ">.");
    }

    this.nameColumn = this.extraColumns.get(nameCol);
    this.extraColumns.remove(nameCol);

    // Retrieve values

    this.retrieveTableValues(metadata.getJdbcDatabase(), conn);

    // Retrieve non-persistent values

    this.npConstants.clear();
    for (NonPersistentTag np : this.nonPersistents) {
      this.npConstants.add(new EnumConstant(np.getName(), np.getValue(), np.getName(), this.extraColumns.size()));
    }

    // Check there are no repeated Java constant names

    Set<String> javaConstantNames = new HashSet<String>();

    for (EnumConstant c : this.tableConstants) {
      if (javaConstantNames.contains(c.getJavaConstantName())) {
        throw new InvalidConfigurationFileException(this,
            "Duplicate constant name '" + c.getJavaConstantName() + "' found in column '" + this.nameCol
                + "' of table '" + this.id.getRenderedSQLName() + "' for <" + super.getTagName() + "> tag. "
                + "Multiple rows of the table produce the same Java constant name for the enum.");
      }
      javaConstantNames.add(c.getJavaConstantName());
    }

    for (EnumConstant c : this.npConstants) {
      if (javaConstantNames.contains(c.getJavaConstantName())) {
        throw new InvalidConfigurationFileException(this,
            "Duplicate constant name '" + c.getJavaConstantName() + "' found in column '" + this.nameCol
                + "' of table '" + this.id.getRenderedSQLName() + "' for <" + super.getTagName() + "> tag. "
                + "A <non-persistent> tag was defined with an already existing name.");
      }
      javaConstantNames.add(c.getJavaConstantName());
    }

    for (SelectMethodTag s : this.getSelects()) {
      s.validateAgainstDatabase(metadata);
    }

  }

  private void retrieveTableValues(final JdbcDatabase db, final Connection conn)
      throws InvalidConfigurationFileException {

    this.tableConstants = new ArrayList<EnumConstant>();

    Id valueId = this.valueColumn.getId();
    Id nameId = this.nameColumn.getId();

    ListWriter lw = new ListWriter(", ");
    lw.add(valueId.getRenderedSQLName());
    lw.add(nameId.getRenderedSQLName());
    for (EnumColumn ec : this.extraColumns.values()) {
      lw.add(ec.getId().getRenderedSQLName());
    }

    String sql = "select " + lw.toString() + " from " + this.id.getRenderedSQLName() + " order by "
        + valueId.getRenderedSQLName();

    // log.info("SQL=" + sql);

    PreparedStatement st = null;
    ResultSet rs = null;

    try {
      st = conn.prepareStatement(sql);
      // log.info("[SQL prepared]");
      rs = st.executeQuery();
      // log.info("[SQL executed]");
      while (rs.next()) {
        Object value = this.valueColumn.getValueTypeManager().getFromResultSet(rs, 1);
        String name = rs.getString(2);
        if (SUtil.isEmpty(name)) {
          throw new InvalidConfigurationFileException(this,
              "Invalid enum constant name from table '" + this.id.getRenderedSQLName() + "' on the <"
                  + super.getTagName() + "> tag. " + "A en empty value was read from the column '" + this.nameCol
                  + "'. " + "If a column is used for the enum constant names, it cannot have null or empty values.");
        }
        String javaConstantName = new TitleIdentifier(name).getJavaConstantName();

        List<String> literalValues = new ArrayList<String>();

        literalValues.add(this.valueColumn.getValueTypeManager().renderJavaValue(value));
        literalValues.add(this.nameColumn.getValueTypeManager().renderJavaValue(name));
        int col = 3;
        for (EnumColumn ec : this.extraColumns.values()) {
          Object v = ec.getValueTypeManager().getFromResultSet(rs, col++);
          literalValues.add(ec.getValueTypeManager().renderJavaValue(v));
        }

        this.tableConstants.add(new EnumConstant(javaConstantName, literalValues));
      }
    } catch (SQLException e) {
      throw new InvalidConfigurationFileException(this, "Could not retrieve table values from table '"
          + this.id.getRenderedSQLName() + "' on the <" + super.getTagName() + "> tag. " + e.getMessage());
    } finally {
      closeResources(st, rs);
    }

  }

  private void closeResources(final PreparedStatement st, final ResultSet rs) {
    if (rs != null) {
      try {
        rs.close();
      } catch (SQLException e) {
        // swallow this exception
      } finally {
        if (st != null) {
          try {
            st.close();
          } catch (SQLException e) {
            // swallow this exception
          }
        }
      }
    }
  }

  private ValueTypeManager<?> resolveValueTypeManager(final JdbcColumn c, final DatabaseAdapter adapter)
      throws InvalidConfigurationFileException {
    PropertyType type;
    try {
      ColumnMetadata cm = new ColumnMetadata(null, c, adapter, null, false, false, this.config.getTypeSolverTag(),
          this.config.getNameSolverTag());
      type = adapter.getAdapterDefaultType(cm);
    } catch (UnresolvableDataTypeException e) {
      throw new InvalidConfigurationFileException(this, "Could not resolve a suitable Java type for the column '"
          + c.getName() + "' on table '" + this.id.getRenderedSQLName() + "'.");
    } catch (InvalidIdentifierException e) {
      throw new InvalidConfigurationFileException(this, "Could not resolve a suitable indentifier for the column '"
          + c.getName() + "' on table '" + this.id.getRenderedSQLName() + "'.");
    }

    String valueType = type.getJavaClassName();
    ValueTypeManager<?> m = ValueTypeFactory.getValueManager(valueType);
    if (m == null) {
      throw new InvalidConfigurationFileException(this,
          "Invalid Java type '" + valueType + "' for the column '" + c.getName() + "' on table '"
              + this.id.getRenderedSQLName()
              + "'. The column must be readable as (i.e. must correspond to) one of the following Java types:\n"
              + ListWriter.render(ValueTypeFactory.getSupportedTypes(), "", " - ", "", "\n", ""));
    }
    return m;

  }

  public static class EnumConstant implements Serializable {

    private static final long serialVersionUID = 1L;

    private String javaConstantName;
    private List<String> javaLiteralValues;

    // From database
    public EnumConstant(final String javaConstantName, final List<String> javaLiteralValue) {
      this.javaConstantName = javaConstantName;
      this.javaLiteralValues = javaLiteralValue;
    }

    // From <non-persistent> tag
    public EnumConstant(final String javaConstantName, final String javaLiteralValue, final String javaLiteralName,
        final int numberOfExtraColumns) {
      this.javaConstantName = javaConstantName;
      this.javaLiteralValues = new ArrayList<String>();
      this.javaLiteralValues.add(javaLiteralValue);
      this.javaLiteralValues.add("\"" + SUtil.escapeJavaString(javaLiteralName) + "\"");
      for (int i = 0; i < numberOfExtraColumns; i++) {
        this.javaLiteralValues.add("null");
      }
    }

    public String getJavaConstantName() {
      return javaConstantName;
    }

    public List<String> getJavaLiteralValues() {
      return javaLiteralValues;
    }

  }

  public static final String VALID_LEAD_JAVA_IDENTIFIER_CHAR = "[a-zA-Z]";
  public static final String VALID_JAVA_IDENTIFIER_CHAR = "[a-zA-Z0-9_]";

  private static class TitleIdentifier {

    private String javaConstantName;

    public TitleIdentifier(final String title) {
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < title.length(); i++) {
        String c = title.substring(i, i + 1);

        if (i == 0) {
          if (!c.matches(VALID_LEAD_JAVA_IDENTIFIER_CHAR)) {
            sb.append("_");
          }
          if (c.matches(VALID_JAVA_IDENTIFIER_CHAR)) {
            sb.append(c.toUpperCase());
          }
        } else if (c.matches(VALID_JAVA_IDENTIFIER_CHAR)) {
          sb.append(c.toUpperCase());
        } else {
          sb.append("_");
        }

      }
      this.javaConstantName = sb.toString();
    }

    public String getJavaConstantName() {
      return this.javaConstantName;
    }

  }

  // Finders

  public ColumnTag findColumnTag(final String jdbcName, final DatabaseAdapter adapter) {
    return null;
  }

  // Getters

  public ObjectId getId() {
    return id;
  }

  public DatabaseObject getDatabaseObjectId() {
    String c = this.id.getCatalog() == null ? null : this.id.getCatalog().getCanonicalSQLName();
    String s = this.id.getSchema() == null ? null : this.id.getSchema().getCanonicalSQLName();
    String n = this.id.getObject().getCanonicalSQLName();
    return new DatabaseObject(c, s, n);
  }

  public String getJdbcName() {
    return this.id.getCanonicalSQLName();
  }

  public List<EnumConstant> getEnumConstants() {
    List<EnumConstant> all = new ArrayList<EnumConstant>();
    all.addAll(this.tableConstants);
    all.addAll(this.npConstants);
    return all;
  }

  public List<EnumConstant> getTableConstants() {
    return this.tableConstants;
  }

  public HotRodFragmentConfigTag getFragmentConfig() {
    return fragmentConfig;
  }

  public static class EnumProperty {

    private String className;
    private String name;

    public EnumProperty(final String className, final String name) {
      this.className = className;
      this.name = name;
    }

    public String getClassName() {
      return className;
    }

    public String getName() {
      return name;
    }

    public String getGetter() {
      return "get" + SUtil.sentenceFormat(this.name);
    }

  }

  public EnumProperty getValueColumn() {
    return this.valueColumn.getProperty();
  }

  public List<EnumProperty> getProperties() {
    List<EnumProperty> props = new ArrayList<EnumProperty>();
    props.add(this.valueColumn.getProperty());
    props.add(this.nameColumn.getProperty());
    for (EnumColumn ec : this.extraColumns.values()) {
      props.add(ec.getProperty());
    }
    return props;
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
    return this.getTagName() + ":" + this.name;
  }

}
