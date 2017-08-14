package org.hotrod.config;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.database.PropertyType;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.UnresolvableDataTypeException;
import org.hotrod.metadata.ColumnMetadata;
import org.hotrod.runtime.util.ListWriter;
import org.hotrod.runtime.util.SUtils;
import org.hotrod.utils.DbUtils;
import org.hotrod.utils.identifiers.DataSetIdentifier;
import org.hotrod.utils.identifiers.DbIdentifier;
import org.hotrod.utils.identifiers.Identifier;
import org.nocrala.tools.database.tartarus.core.JdbcColumn;
import org.nocrala.tools.database.tartarus.core.JdbcDatabase;
import org.nocrala.tools.database.tartarus.core.JdbcTable;

@XmlRootElement(name = "enum")
public class EnumTag extends AbstractConfigurationTag {

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

  private HotRodFragmentConfigTag fragmentConfig = null;

  // Constructor

  public EnumTag() {
    super("enum");
    log.debug("init");
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

  public void validate(final HotRodFragmentConfigTag fragmentConfig) throws InvalidConfigurationFileException {

    this.fragmentConfig = fragmentConfig;

    // name

    if (SUtils.isEmpty(this.name)) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(), "Attribute 'name' of tag <"
          + super.getTagName() + "> cannot be empty. " + "Must specify the name of a database table.");
    }

    // java-name

    if (!SUtils.isEmpty(this.javaClassName)) {
      if (!this.javaClassName.matches(CLASS_NAME_PATTERN)) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(),
            "The attribute 'java-name' of tag <" + super.getTagName() + "> with value '" + this.javaClassName
                + "' is not a valid Java class name. " + "When specified, it must start with an upper case letter, "
                + "and continue with letters, digits, and/or underscores.");
      }
    }

    // name-column

    if (SUtils.isEmpty(this.nameCol)) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(),
          "Attribute 'name-column' of tag <" + super.getTagName() + "> cannot be empty. "
              + "Must specify the column name of the table that will be used as the name for each value for the enum.");
    }

    // non-persistent

    for (NonPersistentTag np : this.nonPersistents) {
      np.validate();
    }

  }

  public static interface ValueTypeManager<T> {

    T getFromResultSet(ResultSet rs, int columnIndex) throws SQLException;

    String renderJavaValue(Object obj);

    String getValueClassName();

  }

  private static Map<String, ValueTypeManager<?>> VALID_VALUE_TYPES = new LinkedHashMap<String, ValueTypeManager<?>>();
  static {
    VALID_VALUE_TYPES.put(Byte.class.getName(), new ValueTypeManager<Byte>() {
      @Override
      public Byte getFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        byte v = rs.getByte(columnIndex);
        return rs.wasNull() ? null : v;
      }

      @Override
      public String renderJavaValue(final Object obj) {
        return obj == null ? "null" : "(byte) " + obj;
      }

      @Override
      public String getValueClassName() {
        return Byte.class.getName();
      }

    });
    VALID_VALUE_TYPES.put(Short.class.getName(), new ValueTypeManager<Short>() {
      @Override
      public Short getFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        short v = rs.getShort(columnIndex);
        return rs.wasNull() ? null : v;
      }

      @Override
      public String renderJavaValue(final Object obj) {
        return obj == null ? "null" : "(short) " + obj;
      }

      @Override
      public String getValueClassName() {
        return Short.class.getName();
      }

    });
    VALID_VALUE_TYPES.put(Integer.class.getName(), new ValueTypeManager<Integer>() {
      @Override
      public Integer getFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        int v = rs.getInt(columnIndex);
        return rs.wasNull() ? null : v;
      }

      @Override
      public String renderJavaValue(final Object obj) {
        return obj == null ? "null" : "" + obj;
      }

      @Override
      public String getValueClassName() {
        return Integer.class.getName();
      }

    });
    VALID_VALUE_TYPES.put(Long.class.getName(), new ValueTypeManager<Long>() {
      @Override
      public Long getFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        long v = rs.getLong(columnIndex);
        return rs.wasNull() ? null : v;
      }

      @Override
      public String renderJavaValue(final Object obj) {
        return obj == null ? "null" : "" + obj + "L";
      }

      @Override
      public String getValueClassName() {
        return Long.class.getName();
      }

    });
    VALID_VALUE_TYPES.put(Double.class.getName(), new ValueTypeManager<Double>() {
      @Override
      public Double getFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        double v = rs.getDouble(columnIndex);
        return rs.wasNull() ? null : v;
      }

      @Override
      public String renderJavaValue(final Object obj) {
        return obj == null ? "null" : "" + obj;
      }

      @Override
      public String getValueClassName() {
        return Double.class.getName();
      }

    });
    VALID_VALUE_TYPES.put(Float.class.getName(), new ValueTypeManager<Float>() {
      @Override
      public Float getFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        float v = rs.getFloat(columnIndex);
        return rs.wasNull() ? null : v;
      }

      @Override
      public String renderJavaValue(final Object obj) {
        return obj == null ? "null" : "" + obj + "f";
      }

      @Override
      public String getValueClassName() {
        return Float.class.getName();
      }

    });
    VALID_VALUE_TYPES.put(BigDecimal.class.getName(), new ValueTypeManager<BigDecimal>() {
      @Override
      public BigDecimal getFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        BigDecimal v = rs.getBigDecimal(columnIndex);
        return rs.wasNull() ? null : v;
      }

      @Override
      public String renderJavaValue(final Object obj) {
        return obj == null ? "null" : "new java.math.BigDecimal(\"" + obj.toString() + "\")";
      }

      @Override
      public String getValueClassName() {
        return BigDecimal.class.getName();
      }

    });

    VALID_VALUE_TYPES.put(String.class.getName(), new ValueTypeManager<String>() {
      @Override
      public String getFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        String v = rs.getString(columnIndex);
        return rs.wasNull() ? null : v;
      }

      @Override
      public String renderJavaValue(final Object obj) {
        return obj == null ? "null" : "\"" + SUtils.escapeJavaString((String) obj) + "\"";
      }

      @Override
      public String getValueClassName() {
        return String.class.getName();
      }

    });
    VALID_VALUE_TYPES.put(Boolean.class.getName(), new ValueTypeManager<Boolean>() {
      @Override
      public Boolean getFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        boolean v = rs.getBoolean(columnIndex);
        return rs.wasNull() ? null : v;
      }

      @Override
      public String renderJavaValue(final Object obj) {
        return obj == null ? "null" : "" + obj;
      }

      @Override
      public String getValueClassName() {
        return Boolean.class.getName();
      }

    });
    VALID_VALUE_TYPES.put(java.sql.Date.class.getName(), new ValueTypeManager<java.sql.Date>() {
      @Override
      public java.sql.Date getFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        Date v = rs.getDate(columnIndex);
        return rs.wasNull() ? null : v;
      }

      @Override
      public String renderJavaValue(final Object obj) {
        return obj == null ? "null" : "java.sql.Date.valueOf(\"" + obj.toString() + "\")";
      }

      @Override
      public String getValueClassName() {
        return java.sql.Date.class.getName();
      }

    });
    VALID_VALUE_TYPES.put(java.sql.Timestamp.class.getName(), new ValueTypeManager<java.sql.Timestamp>() {
      @Override
      public java.sql.Timestamp getFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        Timestamp v = rs.getTimestamp(columnIndex);
        return rs.wasNull() ? null : v;
      }

      @Override
      public String renderJavaValue(final Object obj) {
        return obj == null ? "null" : "java.sql.Timestamp.valueOf(\"" + obj.toString() + "\")";
      }

      @Override
      public String getValueClassName() {
        return java.sql.Timestamp.class.getName();
      }

    });
    VALID_VALUE_TYPES.put(java.util.Date.class.getName(), new ValueTypeManager<java.util.Date>() {
      @Override
      public java.util.Date getFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        Timestamp v = rs.getTimestamp(columnIndex);
        return rs.wasNull() ? null : v;
      }

      @Override
      public String renderJavaValue(final Object obj) {
        java.util.Date d = (java.util.Date) obj;
        return obj == null ? "null" : "new java.util.Date(" + d.getTime() + "L)";
      }

      @Override
      public String getValueClassName() {
        return java.util.Date.class.getName();
      }

    });
  }

  private static class EnumColumn {

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

  public void validateAgainstDatabase(final JdbcDatabase db, final DatabaseAdapter adapter)
      throws InvalidConfigurationFileException {

    // Validate the table existence

    this.table = DbUtils.findTable(db, this.name, adapter);
    if (this.table == null) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(), "Could not find table '" + this.name
          + "' as specified in the attribute 'name' of the tag <" + super.getTagName() + ">.");
    }

    // Get the list of columns

    this.extraColumns = new LinkedHashMap<JdbcColumn, EnumColumn>();

    for (JdbcColumn c : this.table.getColumns()) {
      ValueTypeManager<?> m = resolveValueTypeManager(c, adapter);
      this.extraColumns.put(c, new EnumColumn(c, m));
    }

    // Validate the single-column PK

    if (this.table.getPk() == null) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(),
          "Enum table '" + this.name + "' does not have a primary key. "
              + "A primary key is required on a table that is being used in an <" + super.getTagName() + "> tag.");
    }
    if (this.table.getPk().getKeyColumns().size() != 1) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(),
          "Enum table '" + this.name + "' should not have a composite primary key. "
              + "A single-column primary key is required on a table that is being used in an <" + super.getTagName()
              + "> tag, but this one has " + this.table.getPk().getKeyColumns().size()
              + " columns in the primary key.");
    }

    JdbcColumn pkCol = this.table.getPk().getKeyColumns().get(0).getColumn();
    this.valueColumn = this.extraColumns.get(pkCol);
    this.extraColumns.remove(pkCol);

    // Validate the name column

    JdbcColumn nameCol = DbUtils.findColumn(this.table, this.nameCol, adapter);
    if (nameCol == null) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(),
          "Could not find column '" + this.nameCol + "' on table '" + this.name
              + "' as specified in the attribute 'name-column' of the tag <" + super.getTagName() + ">.");
    }

    this.nameColumn = this.extraColumns.get(nameCol);
    this.extraColumns.remove(nameCol);

    // Retrieve values

    this.retrieveTableValues(db);

    // Retrieve non-persistent values

    for (NonPersistentTag np : this.nonPersistents) {
      this.npConstants.add(new EnumConstant(np.getName(), np.getValue(), np.getName(), this.extraColumns.size()));
    }

    // Check there are no repeated Java constant names

    Set<String> javaConstantNames = new HashSet<String>();

    for (EnumConstant c : this.tableConstants) {
      if (javaConstantNames.contains(c.getJavaConstantName())) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(),
            "Duplicate constant name '" + c.getJavaConstantName() + "' found in column '" + this.nameCol
                + "' of table '" + this.name + "' for <" + super.getTagName() + "> tag. "
                + "Multiple rows of the table produce the same Java constant name for the enum.");
      }
      javaConstantNames.add(c.getJavaConstantName());
    }

    for (EnumConstant c : this.npConstants) {
      if (javaConstantNames.contains(c.getJavaConstantName())) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(),
            "Duplicate constant name '" + c.getJavaConstantName() + "' found in column '" + this.nameCol
                + "' of table '" + this.name + "' for <" + super.getTagName() + "> tag. "
                + "A <non-persistent> tag was defined with an already existing name.");
      }
      javaConstantNames.add(c.getJavaConstantName());
    }

  }

  private void retrieveTableValues(final JdbcDatabase db) throws InvalidConfigurationFileException {

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
      st = db.getDatabaseMetaData().getConnection().prepareStatement(sql);
      // log.info("[SQL prepared]");
      rs = st.executeQuery();
      // log.info("[SQL executed]");
      while (rs.next()) {
        Object value = this.valueColumn.getValueTypeManager().getFromResultSet(rs, 1);
        String name = rs.getString(2);
        if (SUtils.isEmpty(name)) {
          throw new InvalidConfigurationFileException(super.getSourceLocation(),
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
      throw new InvalidConfigurationFileException(super.getSourceLocation(),
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
      ColumnMetadata cm = new ColumnMetadata(c, adapter, null, false, false);
      type = adapter.getAdapterDefaultType(cm);
    } catch (UnresolvableDataTypeException e) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(),
          "Could not resolve a suitable Java type for the column '" + c.getName() + "' on table '" + this.name + "'.");
    }

    String valueType = type.getJavaClassName();
    if (!VALID_VALUE_TYPES.containsKey(valueType)) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(),
          "Invalid Java type '" + valueType + "' for the column '" + c.getName() + "' on table '" + this.name
              + "'. The column must be readable as (i.e. must correspond to) one of the following Java types:\n"
              + ListWriter.render(VALID_VALUE_TYPES.keySet(), "", " - ", "", "\n", ""));
    }
    return VALID_VALUE_TYPES.get(valueType);

  }

  public static class EnumConstant {

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

  public HotRodFragmentConfigTag getFragmentConfig() {
    return fragmentConfig;
  }

  public String getJavaClassName() {
    if (this.javaClassName == null) {
      return new DataSetIdentifier(this.name).getJavaClassIdentifier();
    } else {
      return new DataSetIdentifier(this.name, this.javaClassName).getJavaClassIdentifier();
    }
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

  public List<EnumProperty> getProperties() {
    List<EnumProperty> props = new ArrayList<EnumProperty>();
    props.add(this.valueColumn.getProperty());
    props.add(this.nameColumn.getProperty());
    for (EnumColumn ec : this.extraColumns.values()) {
      props.add(ec.getProperty());
    }
    return props;
  }

}
