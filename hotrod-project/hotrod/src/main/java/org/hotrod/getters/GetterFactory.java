package org.hotrod.getters;

import java.util.HashMap;
import java.util.Map;

public class GetterFactory {

  private static final Map<String, PrimitiveJDBCGetter> PRIMITIVE_JDBC_GETTERS = new HashMap<>();
  static {
    PRIMITIVE_JDBC_GETTERS.put("java.math.BigDecimal",
        PrimitiveJDBCGetter.obj("getBigDecimal", new BigDecimalGetter()));
    PRIMITIVE_JDBC_GETTERS.put("java.sql.Blob", PrimitiveJDBCGetter.obj("getBlob", new BlobGetter()));
    PRIMITIVE_JDBC_GETTERS.put("java.lang.Byte", PrimitiveJDBCGetter.prim("getByte", new ByteGetter()));
    PRIMITIVE_JDBC_GETTERS.put("byte[]", PrimitiveJDBCGetter.obj("getBytes", new BytesGetter()));
    PRIMITIVE_JDBC_GETTERS.put("java.sql.Clob", PrimitiveJDBCGetter.obj("getClob", new ClobGetter()));
    PRIMITIVE_JDBC_GETTERS.put("java.sql.Date", PrimitiveJDBCGetter.obj("getDate", new DateGetter()));
    PRIMITIVE_JDBC_GETTERS.put("java.lang.Double", PrimitiveJDBCGetter.prim("getDouble", new DoubleGetter()));
    PRIMITIVE_JDBC_GETTERS.put("java.lang.Float", PrimitiveJDBCGetter.prim("getFloat", new FloatGetter()));
    PRIMITIVE_JDBC_GETTERS.put("java.lang.Integer", PrimitiveJDBCGetter.prim("getInt", new IntegerGetter()));
    PRIMITIVE_JDBC_GETTERS.put("java.lang.Long", PrimitiveJDBCGetter.prim("getLong", new LongGetter()));
    PRIMITIVE_JDBC_GETTERS.put("java.lang.Boolean", PrimitiveJDBCGetter.prim("getBoolean", new BooleanGetter()));
    PRIMITIVE_JDBC_GETTERS.put("java.lang.Short", PrimitiveJDBCGetter.prim("getShort", new ShortGetter()));
    PRIMITIVE_JDBC_GETTERS.put("java.sql.SQLXML", PrimitiveJDBCGetter.obj("getSQLXML", new SQLXMLGetter()));
    PRIMITIVE_JDBC_GETTERS.put("java.lang.String", PrimitiveJDBCGetter.obj("getString", new StringGetter()));
    PRIMITIVE_JDBC_GETTERS.put("java.sql.Time", PrimitiveJDBCGetter.obj("getTime", new TimeGetter()));
    PRIMITIVE_JDBC_GETTERS.put("java.sql.Timestamp", PrimitiveJDBCGetter.obj("getTimestamp", new TimestampGetter()));
  }

  public static PrimitiveJDBCGetter primitiveGetterForClassName(String className) {
    return PRIMITIVE_JDBC_GETTERS.get(className);
  }

  public static ResultSetGetter forClass(Class<?> c) {
    PrimitiveJDBCGetter g = PRIMITIVE_JDBC_GETTERS.get(c.getName());
    if (g != null) {
      return g.getResultSetGetter();
    }
    return new ObjectGetter(c);
  }

}
