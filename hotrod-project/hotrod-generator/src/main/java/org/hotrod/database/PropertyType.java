package org.hotrod.database;

import java.util.logging.Logger;

import org.hotrod.config.ConverterTag;
import org.hotrod.livesql.queries.typesolver.TypeSource;
import org.hotrod.metadata.ColumnMetadata;
import org.hotrod.typesolver.UnresolvableDataTypeException;
import org.hotrod.utils.JDBCTypes;
import org.hotrod.utils.JDBCTypes.JDBCType;

public class PropertyType {

  private static final Logger log = Logger.getLogger(PropertyType.class.getName());

  private static final String DIALECT = "D";
  private static final String TYPESOLVER = "T";

  private String javaClassName;
  private ConverterTag converterTag;
  private JDBCType jdbcType;
  private boolean isLOB;
  private TypeSource typeSource;
  private String namespace;
  private Integer ruleNumber;
  private ValueRange valueRange;

  // Constructors for internal types

  /* Internal type for a serial column */
  public PropertyType(final Class<?> javaClass, final ColumnMetadata m, final boolean isLOB,
      final ValueRange valueRange, final TypeSource typeSource, final Integer ruleNumber)
      throws UnresolvableDataTypeException {
    log.fine("init");
    JDBCType t = JDBCTypes.codeToType(m.getDataType());
    // log.info("a) code=" + m.getDataType() + " type=" + t);
    if (t == null) {
      throw new UnresolvableDataTypeException(m);
    }
    initialize(javaClass.getName(), t, isLOB, valueRange, typeSource, null, ruleNumber, DIALECT);
  }

  /* Internal type for a non-serial column */
  public PropertyType(final Class<?> javaClass, final ColumnMetadata m, final boolean isLOB,
      final TypeSource typeSource, final Integer ruleNumber) throws UnresolvableDataTypeException {
    JDBCType t = JDBCTypes.codeToType(m.getDataType());
    // log.info("b) code=" + m.getDataType() + " type=" + t);
    if (t == null) {
      throw new UnresolvableDataTypeException(m);
    }
    initialize(javaClass.getName(), t, isLOB, null, typeSource, null, ruleNumber, DIALECT);
  }

  /*
   * Internal type for a non-serial column with non-standard JDBC type reported by
   * the JDBC driver
   */
  public PropertyType(final Class<?> javaClass, final JDBCType jdbcType, final boolean isLOB,
      final TypeSource typeSource, final Integer ruleNumber) {
    initialize(javaClass.getName(), jdbcType, isLOB, null, typeSource, null, ruleNumber, DIALECT);
  }

  // For custom data types and select parameters

  /* Custom type for a non-serial column with unspecified JDBC type */
  public PropertyType(final String javaClassName, final ColumnMetadata m, final boolean isLOB,
      final TypeSource typeSource, final Integer ruleNumber) throws UnresolvableDataTypeException {
    JDBCType t = JDBCTypes.codeToType(m.getDataType());
    // log.info("c) code=" + m.getDataType() + " type=" + t);
    if (t == null) {
      throw new UnresolvableDataTypeException(m);
    }
    initialize(javaClassName, t, isLOB, null, typeSource, null, ruleNumber, DIALECT);
  }

  /* Custom type for a non-serial column with specified JDBC type */
  public PropertyType(final String javaClassName, final JDBCType jdbcType, final boolean isLOB,
      final TypeSource typeSource, final ConverterTag converterTag, final Integer ruleNumber) {
    initialize(javaClassName, jdbcType, isLOB, null, typeSource, converterTag, ruleNumber, DIALECT);
  }

  public PropertyType(final String javaClassName, final JDBCType jdbcType, final boolean isLOB,
      final TypeSource typeSource, final ConverterTag converterTag, final Integer ruleNumber, boolean fromTypeSolver) {
    initialize(javaClassName, jdbcType, isLOB, null, typeSource, converterTag, ruleNumber,
        fromTypeSolver ? TYPESOLVER : DIALECT);
  }

  /* Custom type for a serial column */
  public PropertyType(final String javaClassName, final JDBCType jdbcType, final boolean isLOB,
      final ValueRange valueRange, final TypeSource typeSource, final ConverterTag converterTag,
      final Integer ruleNumber) {
    initialize(javaClassName, jdbcType, isLOB, valueRange, typeSource, converterTag, ruleNumber, null);
  }

  // Initialize

  private void initialize(final String javaClassName, final JDBCType jdbcType, final boolean isLOB,
      final ValueRange valueRange, TypeSource typeSource, final ConverterTag converterTag, final Integer ruleNumber,
      String namespace) {
    this.javaClassName = converterTag == null ? javaClassName : converterTag.getDomainClass();
    this.converterTag = converterTag;
    this.jdbcType = jdbcType;
    this.isLOB = isLOB;
    this.typeSource = typeSource;
    this.ruleNumber = ruleNumber;
    this.valueRange = valueRange;
    this.namespace = namespace;
  }

  // ToString

  public String toString() {
    return "javaClass=" + this.javaClassName + ", converter=" + this.converterTag + ", jdbcType=" + this.jdbcType
        + ", isLOB=" + this.isLOB + ", valueRange=" + this.valueRange;
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

  public final ConverterTag getConverterTag() {
    return converterTag;
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

  public final TypeSource getTypeSource() {
    return typeSource;
  }

  public final String getRuleNumber() {
    return this.ruleNumber == null ? null : this.namespace + ruleNumber;
  }

}
