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
  private String valueCol = null;
  private String nameCol = null;
  private List<NonPersistentTag> nonPersistents = new ArrayList<NonPersistentTag>();

  private JdbcTable table = null;
  private JdbcColumn valueColumn = null;
  private JdbcColumn nameColumn = null;

  private List<EnumConstant> tableConstants = new ArrayList<EnumConstant>();
  private List<EnumConstant> npConstants = new ArrayList<EnumConstant>();

  private String valueType = null;
  private ValueTypeManager<?> valueTypeManager = null;

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

  @XmlAttribute(name = "class")
  public void setJavaClass(final String javaClass) {
    this.javaClassName = javaClass;
  }

  @XmlAttribute(name = "value-column")
  public void setValueCol(final String valueCol) {
    this.valueCol = valueCol;
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

    // class

    if (SUtils.isEmpty(this.javaClassName)) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(),
          "Attribute 'class' of tag <" + super.getTagName() + "> cannot be empty. "
              + "Must specify the name of the enum class to be generated, including the full package.");
    }
    if (!this.javaClassName.matches(CLASS_NAME_PATTERN)) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(),
          "Attribute 'class' of tag <" + super.getTagName() + "> is not a valid Java class name. "
              + "Must start with an upper case letter, and continue with letters, digits, and/or underscores.");
    }

    // value-column

    if (SUtils.isEmpty(this.valueCol)) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(),
          "Attribute 'value-column' of tag <" + super.getTagName() + "> cannot be empty. "
              + "Must specify the column name of the table, that will be used to identify each value for the enum.");
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
        return "" + obj;
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
        return "" + obj;
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
        return "" + obj;
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
        return "" + obj;
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
        return "" + obj;
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
        return "" + obj;
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
        return "new java.math.BigDecimal(\"" + obj.toString() + "\")";
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
        return "\"" + SUtils.escapeJavaString((String) obj) + "\"";
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
        return "" + obj;
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
  }

  public void validateAgainstDatabase(final JdbcDatabase db, final DatabaseAdapter adapter)
      throws InvalidConfigurationFileException {

    this.table = DbUtils.findTable(db, this.name, adapter);
    if (this.table == null) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(), "Could not find table '" + this.name
          + "' as specified in the attribute 'name' of the tag <" + super.getTagName() + ">.");
    }

    this.valueColumn = DbUtils.findColumn(this.table, this.valueCol, adapter);
    if (this.valueColumn == null) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(),
          "Could not find table column '" + this.valueCol + "' on table '" + this.name
              + "' as specified in the attribute 'value-column' of the tag <" + super.getTagName() + ">.");
    }

    this.nameColumn = DbUtils.findColumn(this.table, this.nameCol, adapter);
    if (this.nameColumn == null) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(),
          "Could not find table column '" + this.nameCol + "' on table '" + this.name
              + "' as specified in the attribute 'name-column' of the tag <" + super.getTagName() + ">.");
    }

    // Retrieve values

    PropertyType type;
    try {
      ColumnMetadata cm = new ColumnMetadata(this.valueColumn, adapter, null, false, false);
      type = adapter.getAdapterDefaultType(cm);
    } catch (UnresolvableDataTypeException e) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(),
          "Could not resolve a suitable Java type for the column '" + this.valueCol + "' on table '" + this.name
              + "' as specified in the attribute 'value-column' of the tag <" + super.getTagName() + ">.");
    }

    this.valueType = type.getJavaClassName();
    if (!VALID_VALUE_TYPES.containsKey(this.valueType)) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(),
          "Invalid Java type for the column '" + this.valueCol + "' on table '" + this.name
              + "' as specified in the attribute 'value-column' of the tag <" + super.getTagName()
              + ">. The column must be readable as (i.e. must correspond to) one of the following Java types:\n"
              + ListWriter.render(VALID_VALUE_TYPES.keySet(), "", " - ", "", "\n", ""));
    }
    this.valueTypeManager = VALID_VALUE_TYPES.get(this.valueType);

    this.retrieveTableValues(db);

    // Retrieve non-persistent values

    for (NonPersistentTag np : this.nonPersistents) {
      this.npConstants.add(new EnumConstant(np.getValue(), np.getName(), np.getName()));
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
    Identifier valueId = new DbIdentifier(this.valueCol);
    Identifier nameId = new DbIdentifier(this.nameCol);

    String sql = "select " + valueId.getSQLIdentifier() + ", " + nameId.getSQLIdentifier() + " from "
        + tableId.getSQLIdentifier() + " order by " + valueId.getSQLIdentifier();

    // log.info("SQL=" + sql);

    PreparedStatement st = null;
    ResultSet rs = null;

    try {
      st = db.getDatabaseMetaData().getConnection().prepareStatement(sql);
      // log.info("[SQL prepared]");
      rs = st.executeQuery();
      // log.info("[SQL executed]");
      while (rs.next()) {
        Object value = this.valueTypeManager.getFromResultSet(rs, 1);
        String name = rs.getString(2);
        String javaConstantName = new TitleIdentifier(name).getJavaConstantName();
        // log.info("value=" + value + " name=" + name + " constant=" +
        // javaConstantName);
        this.tableConstants.add(new EnumConstant(value, name, javaConstantName));
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

  public static class EnumConstant {

    private Object value;
    private String title;
    private String javaConstantName;

    public EnumConstant(final Object value, final String title, final String javaConstantName) {
      this.value = value;
      this.title = title;
      this.javaConstantName = javaConstantName;
    }

    public Object getValue() {
      return value;
    }

    public String getTitle() {
      return title;
    }

    public String getJavaConstantName() {
      return javaConstantName;
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
    return javaClassName;
  }

  public ValueTypeManager<?> getValueTypeManager() {
    return valueTypeManager;
  }

}
