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

import org.apache.log4j.Logger;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.database.PropertyType;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.UnresolvableDataTypeException;
import org.hotrod.generator.HotRodGenerator;
import org.hotrod.metadata.ColumnMetadata;
import org.hotrod.runtime.util.ListWriter;
import org.hotrod.runtime.util.SUtils;
import org.hotrod.utils.ClassPackage;
import org.hotrod.utils.Compare;
import org.hotrod.utils.ValueTypeFactory;
import org.hotrod.utils.ValueTypeFactory.ValueTypeManager;
import org.hotrod.utils.identifiers.DataSetIdentifier;
import org.hotrod.utils.identifiers.DbIdentifier;
import org.hotrod.utils.identifiers.Identifier;
import org.nocrala.tools.database.tartarus.core.JdbcColumn;
import org.nocrala.tools.database.tartarus.core.JdbcDatabase;
import org.nocrala.tools.database.tartarus.core.JdbcTable;

@XmlRootElement(name = "enum")
public class EnumTag extends AbstractEntityDAOTag {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = Logger.getLogger(EnumTag.class);

  private static final String CLASS_NAME_PATTERN = "[A-Z][a-zA-Z0-9_]*";

  // Properties

  private String name = null;
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
  private HotRodFragmentConfigTag fragmentConfig = null;
  private ClassPackage fragmentPackage;

  // Constructor

  public EnumTag() {
    super("enum");
    log.debug("init");
  }

  // Duplicate

  public EnumTag duplicate() {
    EnumTag d = new EnumTag();

    d.copyCommon(this);

    d.name = this.name;
    d.javaClassName = this.javaClassName;
    d.nameCol = this.nameCol;
    d.nonPersistents = this.nonPersistents;
    d.table = this.table;
    d.valueColumn = this.valueColumn;
    d.nameColumn = this.nameColumn;
    d.tableConstants = this.tableConstants;
    d.npConstants = this.npConstants;
    d.extraColumns = this.extraColumns;

    d.daosTag = this.daosTag;
    d.fragmentConfig = this.fragmentConfig;
    d.fragmentPackage = this.fragmentPackage;

    return d;
  }

  // JAXB Setters

  @XmlAttribute
  public void setName(final String column) {
    this.name = column;
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

  public void validate(final DaosTag daosTag, final HotRodFragmentConfigTag fragmentConfig)
      throws InvalidConfigurationFileException {

    this.daosTag = daosTag;
    this.fragmentConfig = fragmentConfig;
    this.fragmentPackage = this.fragmentConfig != null && this.fragmentConfig.getFragmentPackage() != null
        ? this.fragmentConfig.getFragmentPackage() : null;

    // name

    if (SUtils.isEmpty(this.name)) {
      throw new InvalidConfigurationFileException(this, //
          "Attribute 'name' cannot be empty", //
          "Attribute 'name' of tag <" + super.getTagName() + "> cannot be empty. "
              + "Must specify the name of a database table.");
    }

    // java-name

    if (!SUtils.isEmpty(this.javaClassName)) {
      if (!this.javaClassName.matches(CLASS_NAME_PATTERN)) {
        throw new InvalidConfigurationFileException(this, //
            "Invalid attribute 'java-name' with value '" + this.javaClassName
                + "': when specified, must start with an upper case letter, "
                + "and continue with letters, digits, and/or underscores", //
            "The attribute 'java-name' of tag <" + super.getTagName() + "> with value '" + this.javaClassName
                + "' is not a valid Java class name. " + "When specified, it must start with an upper case letter, "
                + "and continue with letters, digits, and/or underscores.");
      }
    }

    // name-column

    if (SUtils.isEmpty(this.nameCol)) {
      throw new InvalidConfigurationFileException(this, //
          "Attribute 'name-column' cannot be empty", //
          "Attribute 'name-column' of tag <" + super.getTagName() + "> cannot be empty. "
              + "Must specify the column name of the table that will be used as the name for each value for the enum.");
    }

    // non-persistent

    for (NonPersistentTag np : this.nonPersistents) {
      np.validate();
    }

  }

  private static class EnumColumn implements Serializable {

    private static final long serialVersionUID = 1L;

    private JdbcColumn column;
    private ValueTypeManager<?> valueTypeManager;

    public EnumColumn(final JdbcColumn column, final ValueTypeManager<?> valueTypeManager) {
      this.column = column;
      this.valueTypeManager = valueTypeManager;
    }

    public JdbcColumn getColumn() {
      return column;
    }

    public ValueTypeManager<?> getValueTypeManager() {
      return valueTypeManager;
    }

    public EnumProperty getProperty() {
      Identifier id = new DbIdentifier(this.column.getName());
      return new EnumProperty(this.valueTypeManager.getValueClassName(), id.getJavaMemberIdentifier());
    }
  }

  public void validateAgainstDatabase(final HotRodGenerator generator, final Connection conn)
      throws InvalidConfigurationFileException {

    // Validate the table existence

    this.table = generator.findJdbcTable(this.name);
    if (this.table == null) {
      throw new InvalidConfigurationFileException(this, //
          "Could not find database table '" + this.name + "'", //
          "Could not find table '" + this.name + "' as specified in the attribute 'name' of the tag <"
              + super.getTagName() + ">.");
    }

    // Get the list of columns

    this.extraColumns = new LinkedHashMap<JdbcColumn, EnumColumn>();

    for (JdbcColumn c : this.table.getColumns()) {
      ValueTypeManager<?> m = resolveValueTypeManager(c, generator.getAdapter());
      this.extraColumns.put(c, new EnumColumn(c, m));
    }

    // Validate the single-column PK

    if (this.table.getPk() == null) {
      throw new InvalidConfigurationFileException(this, //
          "Enum table '" + this.name + "' must have a (single-column) primary key", //
          "Enum table '" + this.name + "' does not have a primary key. "
              + "A primary key is required on a table that is being used in an <" + super.getTagName() + "> tag.");
    }
    if (this.table.getPk().getKeyColumns().size() != 1) {
      throw new InvalidConfigurationFileException(this, //
          "Enum table '" + this.name + "' cannot have a composite primary key", //
          "Enum table '" + this.name + "' should not have a composite primary key. "
              + "A single-column primary key is required on a table that is being used in an <" + super.getTagName()
              + "> tag, but this one has " + this.table.getPk().getKeyColumns().size()
              + " columns in the primary key.");
    }

    JdbcColumn pkCol = this.table.getPk().getKeyColumns().get(0).getColumn();
    this.valueColumn = this.extraColumns.get(pkCol);
    this.extraColumns.remove(pkCol);

    // Validate the name column

    JdbcColumn nameCol = generator.findJdbcColumn(this.table, this.nameCol);
    if (nameCol == null) {
      throw new InvalidConfigurationFileException(this, //
          "Could not find column '" + this.nameCol + "' on table '" + this.name + "'", //
          "Could not find column '" + this.nameCol + "' on table '" + this.name
              + "' as specified in the attribute 'name-column' of the tag <" + super.getTagName() + ">.");
    }

    this.nameColumn = this.extraColumns.get(nameCol);
    this.extraColumns.remove(nameCol);

    // Retrieve values

    this.retrieveTableValues(generator.getJdbcDatabase(), conn);

    // Retrieve non-persistent values

    for (NonPersistentTag np : this.nonPersistents) {
      this.npConstants.add(new EnumConstant(np.getName(), np.getValue(), np.getName(), this.extraColumns.size()));
    }

    // Check there are no repeated Java constant names

    Set<String> javaConstantNames = new HashSet<String>();

    for (EnumConstant c : this.tableConstants) {
      if (javaConstantNames.contains(c.getJavaConstantName())) {
        throw new InvalidConfigurationFileException(this, //
            "Duplicate constant name '" + c.getJavaConstantName() + "' found in column '" + this.nameCol
                + "' of table '" + this.name
                + "'.\nMultiple rows of the table produce the same Java constant name for the enum", //
            "Duplicate constant name '" + c.getJavaConstantName() + "' found in column '" + this.nameCol
                + "' of table '" + this.name + "' for <" + super.getTagName() + "> tag. "
                + "Multiple rows of the table produce the same Java constant name for the enum.");
      }
      javaConstantNames.add(c.getJavaConstantName());
    }

    for (EnumConstant c : this.npConstants) {
      if (javaConstantNames.contains(c.getJavaConstantName())) {
        throw new InvalidConfigurationFileException(this, //
            "Duplicate constant name '" + c.getJavaConstantName() + "' found in column '" + this.nameCol
                + "' of table '" + this.name + "'.\nA <non-persistent> tag was defined with an already existing name", //
            "Duplicate constant name '" + c.getJavaConstantName() + "' found in column '" + this.nameCol
                + "' of table '" + this.name + "' for <" + super.getTagName() + "> tag. "
                + "A <non-persistent> tag was defined with an already existing name.");
      }
      javaConstantNames.add(c.getJavaConstantName());
    }

    for (SelectMethodTag s : this.getSelects()) {
      s.validateAgainstDatabase(generator);
    }

  }

  private void retrieveTableValues(final JdbcDatabase db, final Connection conn)
      throws InvalidConfigurationFileException {

    this.tableConstants = new ArrayList<EnumConstant>();

    Identifier tableId = new DbIdentifier(this.name);

    Identifier valueId = new DbIdentifier(this.valueColumn.getColumn().getName());
    Identifier nameId = new DbIdentifier(this.nameColumn.getColumn().getName());

    ListWriter lw = new ListWriter(", ");
    lw.add(valueId.getSQLIdentifier());
    lw.add(nameId.getSQLIdentifier());
    for (EnumColumn ec : this.extraColumns.values()) {
      Identifier id = new DbIdentifier(ec.getColumn().getName());
      lw.add(id.getSQLIdentifier());
    }

    String sql = "select " + lw.toString() + " from " + tableId.getSQLIdentifier() + " order by "
        + valueId.getSQLIdentifier();

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
        if (SUtils.isEmpty(name)) {
          throw new InvalidConfigurationFileException(this, //
              "Enum constant name cannot be empty: an empty value was found on the colum '" + this.nameCol + "'", //
              "Invalid enum constant name from table '" + this.name + "' on the <" + super.getTagName() + "> tag. "
                  + "A en empty value was read from the column '" + this.nameCol + "'. "
                  + "If a column is used for the enum constant names, it cannot have null or empty values.");
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
      throw new InvalidConfigurationFileException(this, //
          "Could not retrieve table values from table '" + this.name + "'", //
          "Could not retrieve table values from table '" + this.name + "' on the <" + super.getTagName() + "> tag. "
              + e.getMessage());
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
      ColumnMetadata cm = new ColumnMetadata(null, c, adapter, null, false, false);
      type = adapter.getAdapterDefaultType(cm);
    } catch (UnresolvableDataTypeException e) {
      throw new InvalidConfigurationFileException(this, //
          "Could not resolve a suitable Java type for the column '" + c.getName() + "'", //
          "Could not resolve a suitable Java type for the column '" + c.getName() + "' on table '" + this.name + "'.");
    }

    String valueType = type.getJavaClassName();
    ValueTypeManager<?> m = ValueTypeFactory.getValueManager(valueType);
    if (m == null) {
      throw new InvalidConfigurationFileException(this, //
          "Invalid Java type '" + valueType + "' for the column '" + c.getName()
              + "': the column must correspond to a simple Java type, as defined in the documentation", //
          "Invalid Java type '" + valueType + "' for the column '" + c.getName() + "' on table '" + this.name
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
      this.javaLiteralValues.add("\"" + SUtils.escapeJavaString(javaLiteralName) + "\"");
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
        if (i == 0 && c.matches(VALID_LEAD_JAVA_IDENTIFIER_CHAR) || i != 0 && c.matches(VALID_JAVA_IDENTIFIER_CHAR)) {
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

  // Indexing methods

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
    EnumTag other = (EnumTag) obj;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    return true;
  }

  // Finders

  public ColumnTag findColumnTag(final String jdbcName, final DatabaseAdapter adapter) {
    return null;
  }

  // Getters

  public String getName() {
    return name;
  }

  public String getJdbcName() {
    return this.table.getName();
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
      return "get" + SUtils.capitalizeFirst(this.name);
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
    if (this.javaClassName == null) {
      return new DataSetIdentifier(this.name).getJavaClassIdentifier();
    } else {
      return new DataSetIdentifier(this.name, this.javaClassName).getJavaClassIdentifier();
    }
  }

  // Merging logic

  @Override
  public boolean sameKey(final AbstractConfigurationTag fresh) {
    try {
      EnumTag f = (EnumTag) fresh;
      return this.name.equals(f.name);
    } catch (ClassCastException e) {
      return false;
    }
  }

  @Override
  public boolean copyNonKeyProperties(final AbstractConfigurationTag fresh) {
    try {
      EnumTag f = (EnumTag) fresh;
      boolean different = !same(fresh);

      this.javaClassName = f.javaClassName;
      this.nameCol = f.nameCol;
      this.nonPersistents = f.nonPersistents;
      this.table = f.table;
      this.valueColumn = f.valueColumn;
      this.nameColumn = f.nameColumn;
      this.tableConstants = f.tableConstants;
      this.npConstants = f.npConstants;
      this.extraColumns = f.extraColumns;
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
      EnumTag f = (EnumTag) fresh;
      boolean same = //
          Compare.same(this.name, f.name) && //
              Compare.same(this.javaClassName, f.javaClassName) && //
              Compare.same(this.nameCol, f.nameCol) && //
              Compare.same(this.nonPersistents, f.nonPersistents);

      return same;
    } catch (ClassCastException e) {
      return false;
    }
  }

  // Simple Caption

  @Override
  public String getInternalCaption() {
    return this.getTagName() + ":" + this.name;
  }

}
