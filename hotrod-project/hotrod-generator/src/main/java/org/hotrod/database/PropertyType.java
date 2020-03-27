package org.hotrod.database;

import java.io.Serializable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.exceptions.UnresolvableDataTypeException;
import org.hotrod.metadata.ColumnMetadata;
import org.hotrod.utils.ColumnUtils;
import org.hotrod.utils.JdbcTypes;
import org.hotrod.utils.JdbcTypes.JDBCType;

public class PropertyType implements Serializable {

  private static final long serialVersionUID = 1L;

  private static final Logger log = LogManager.getLogger(PropertyType.class);

  private String javaClassName;
  private JDBCType jdbcType;
  private boolean isLOB;

  public static class ValueRange implements Serializable {

    private static final long serialVersionUID = 1L;

    public static ValueRange BYTE_RANGE = new ValueRange(0, Byte.MIN_VALUE, Byte.MAX_VALUE);
    public static ValueRange SHORT_RANGE = new ValueRange(0, Short.MIN_VALUE, Short.MAX_VALUE);
    public static ValueRange INTEGER_RANGE = new ValueRange(0, Integer.MIN_VALUE, Integer.MAX_VALUE);
    public static ValueRange LONG_RANGE = new ValueRange(0, Long.MIN_VALUE, Long.MAX_VALUE);

    public static ValueRange UNSIGNED_BYTE_RANGE = new ValueRange(0, 0, 2 * Byte.MAX_VALUE + 1);
    public static ValueRange UNSIGNED_SHORT_RANGE = new ValueRange(0, 0, 2 * Short.MAX_VALUE + 1);
    public static ValueRange UNSIGNED_INTEGER_RANGE = new ValueRange(0, 0, 2L * Integer.MAX_VALUE + 1L);

    private long initialValue;
    private long minValue;
    private long maxValue;

    public ValueRange(long initialValue, long minValue, long maxValue) {
      super();
      log.debug("init");
      this.initialValue = initialValue;
      this.minValue = minValue;
      this.maxValue = maxValue;
    }

    public long getInitialValue() {
      return initialValue;
    }

    public long getMinValue() {
      return minValue;
    }

    public long getMaxValue() {
      return maxValue;
    }

    public static ValueRange getSignedRange(final int size) {
      return new ValueRange(0, ColumnUtils.getMinValue(size), ColumnUtils.getMaxValue(size));
    }

    public static ValueRange getUnsignedRange(final int size) {
      return new ValueRange(0, 0, ColumnUtils.getMaxValue(size));
    }

    public String toString() {
      return "[min=" + this.minValue + ", max=" + this.maxValue + ", initial=" + this.initialValue + "]";
    }

  }

  private ValueRange valueRange;

  // Constructors for internal types

  /* Internal type for a serial column */
  public PropertyType(final Class<?> javaClass, final ColumnMetadata m, final boolean isLOB,
      final ValueRange valueRange) throws UnresolvableDataTypeException {
    JDBCType t = JdbcTypes.codeToType(m.getDataType());
    // log.info("a) code=" + m.getDataType() + " type=" + t);
    if (t == null) {
      throw new UnresolvableDataTypeException(m);
    }
    initialize(javaClass.getName(), t, isLOB, valueRange);
  }

  /* Internal type for a non-serial column */
  public PropertyType(final Class<?> javaClass, final ColumnMetadata m, final boolean isLOB)
      throws UnresolvableDataTypeException {
    JDBCType t = JdbcTypes.codeToType(m.getDataType());
    // log.info("b) code=" + m.getDataType() + " type=" + t);
    if (t == null) {
      throw new UnresolvableDataTypeException(m);
    }
    initialize(javaClass.getName(), t, isLOB, null);
  }

  /*
   * Internal type for a non-serial column with non-standard JDBC type reported by
   * the JDBC driver
   */
  public PropertyType(final Class<?> javaClass, final JDBCType jdbcType, final boolean isLOB) {
    initialize(javaClass.getName(), jdbcType, isLOB, null);
  }

  // For custom data types and select parameters

  /* Custom type for a non-serial column with unspecified JDBC type */
  public PropertyType(final String javaClassName, final ColumnMetadata m, final boolean isLOB)
      throws UnresolvableDataTypeException {
    JDBCType t = JdbcTypes.codeToType(m.getDataType());
    // log.info("c) code=" + m.getDataType() + " type=" + t);
    if (t == null) {
      throw new UnresolvableDataTypeException(m);
    }
    initialize(javaClassName, t, isLOB, null);
  }

  /* Custom type for a non-serial column with specified JDBC type */
  public PropertyType(final String javaClassName, final JDBCType jdbcType, final boolean isLOB) {
    initialize(javaClassName, jdbcType, isLOB, null);
  }

  /* Custom type for a serial column */
  public PropertyType(final String javaClassName, final JDBCType jdbcType, final boolean isLOB,
      final ValueRange valueRange) {
    initialize(javaClassName, jdbcType, isLOB, valueRange);
  }

  // Initialize

  private void initialize(final String javaClassName, final JDBCType jdbcType, final boolean isLOB,
      final ValueRange valueRange) {
    this.javaClassName = javaClassName;
    this.jdbcType = jdbcType;
    this.isLOB = isLOB;
    this.valueRange = valueRange;
  }

  // ToString

  public String toString() {
    return "javaClass=" + this.javaClassName + ", jdbcType=" + this.jdbcType + ", isLOB=" + this.isLOB + ", valueRange="
        + this.valueRange;
  }

  // Getters

  public static ValueRange getDefaultValueRange(final String classJavaType) {
    if ("java.lang.Byte".equals(classJavaType)) {
      return new ValueRange(0, Byte.MIN_VALUE, Byte.MAX_VALUE);
    }
    if ("java.lang.Short".equals(classJavaType)) {
      return new ValueRange(0, Short.MIN_VALUE, Short.MAX_VALUE);
    }
    if ("java.lang.Integer".equals(classJavaType)) {
      return new ValueRange(0, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }
    if ("java.lang.Long".equals(classJavaType)) {
      return new ValueRange(0, Long.MIN_VALUE, Long.MAX_VALUE);
    }
    return null;
  }

  public String getPrimitiveClassJavaType() {
    if (Byte.class.getName().equals(this.javaClassName)) {
      return "byte";
    }
    if (Short.class.getName().equals(this.javaClassName)) {
      return "short";
    }
    if (Integer.class.getName().equals(this.javaClassName)) {
      return "int";
    }
    if (Long.class.getName().equals(this.javaClassName)) {
      return "long";
    }
    if (Float.class.getName().equals(this.javaClassName)) {
      return "float";
    }
    if (Double.class.getName().equals(this.javaClassName)) {
      return "double";
    }
    if (Boolean.class.getName().equals(this.javaClassName)) {
      return "boolean";
    }
    if (Character.class.getName().equals(this.javaClassName)) {
      return "char";
    }
    return null;
  }

  public boolean isBooleanType() {
    return isBooleanType(this.javaClassName);
  }

  public static boolean isBooleanType(final String typeName) {
    return
    // The JavaBean specification only includes the primitive type boolean, and
    // not the class java.lang.Boolean:
    // "java.lang.Boolean".equals(typeName) || //
    // "Boolean".equals(typeName) || //
    "boolean".equals(typeName);
  }

  public boolean isLOB() {
    return isLOB;
  }

  // New methods

  /* Example: "java.lang.Integer" */
  public String getJavaClassName() {
    return this.javaClassName;
  }

  /* Example: "java.sql.Types.NUMERIC" */
  public String getJDBCType() {
    return this.jdbcType.getTypeName();
  }

  /* Example: "NUMERIC" */
  public String getJDBCShortType() {
    return this.jdbcType.getShortTypeName();
  }

  public ValueRange getValueRange() {
    return valueRange;
  }

}
