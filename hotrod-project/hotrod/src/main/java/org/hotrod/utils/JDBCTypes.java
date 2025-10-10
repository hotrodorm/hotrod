package org.hotrod.utils;

import java.util.HashMap;
import java.util.Map;

public class JDBCTypes {

  public static enum JDBCType {

    ARRAY("ARRAY", java.sql.Types.ARRAY), // 2003
    BIGINT("BIGINT", java.sql.Types.BIGINT), // -5
    BINARY("BINARY", java.sql.Types.BINARY), // -2

    BIT("BIT", java.sql.Types.BIT), // -7
    BLOB("BLOB", java.sql.Types.BLOB), // 2004

    BOOLEAN("BOOLEAN", java.sql.Types.BOOLEAN), // 16
    CHAR("CHAR", java.sql.Types.CHAR), // 1
    CLOB("CLOB", java.sql.Types.CLOB), // 2005
    DATALINK("DATALINK", java.sql.Types.DATALINK), // 70
    DATE("DATE", java.sql.Types.DATE), // 91

    DECIMAL("DECIMAL", java.sql.Types.DECIMAL), // 3
    DISTINCT("DISTINCT", java.sql.Types.DISTINCT), // 2001
    DOUBLE("DOUBLE", java.sql.Types.DOUBLE), // 8
    FLOAT("FLOAT", java.sql.Types.FLOAT), // 6
    INTEGER("INTEGER", java.sql.Types.INTEGER), // 4

    JAVA_OBJECT("JAVA_OBJECT", java.sql.Types.JAVA_OBJECT), // 2000
    LONGNVARCHAR("LONGNVARCHAR", java.sql.Types.LONGNVARCHAR), // -16
    LONGVARBINARY("LONGVARBINARY", java.sql.Types.LONGVARBINARY), // -4
    LONGVARCHAR("LONGVARCHAR", java.sql.Types.LONGVARCHAR), // -1
    NCHAR("NCHAR", java.sql.Types.NCHAR), // -15

    NCLOB("NCLOB", java.sql.Types.NCLOB), // 2011
    NULL("NULL", java.sql.Types.NULL), // 0
    NUMERIC("NUMERIC", java.sql.Types.NUMERIC), // 2
    NVARCHAR("NVARCHAR", java.sql.Types.NVARCHAR), // -9
    OTHER("OTHER", java.sql.Types.OTHER), // 1111

    REAL("REAL", java.sql.Types.REAL), // 7
    REF("REF", java.sql.Types.REF), // 2006
    ROWID("ROWID", java.sql.Types.ROWID), // -8
    SMALLINT("SMALLINT", java.sql.Types.SMALLINT), // 5
    SQLXML("SQLXML", java.sql.Types.SQLXML), // 2009

    STRUCT("STRUCT", java.sql.Types.STRUCT), // 2002
    TIME("TIME", java.sql.Types.TIME), // 92
    TIMESTAMP("TIMESTAMP", java.sql.Types.TIMESTAMP), // 93

    TINYINT("TINYINT", java.sql.Types.TINYINT), // -6
    VARBINARY("VARBINARY", java.sql.Types.VARBINARY), // -3

    VARCHAR("VARCHAR", java.sql.Types.VARCHAR), // 12

    // Available since Java 8

    REF_CURSOR("REF_CURSOR", 2012), // 2012
    TIMESTAMP_WITH_TIMEZONE("TIMESTAMP_WITH_TIMEZONE", 2014), // 2014
    TIME_WITH_TIMEZONE("TIME_WITH_TIMEZONE", 2013); // 2013

    private int code;
    private String shortTypeName;
    private String typeName;

    private JDBCType(final String shortTypeName, final int code) {
      this.code = code;
      this.shortTypeName = shortTypeName;
      this.typeName = "java.sql.Types." + shortTypeName;
    }

    public int getCode() {
      return code;
    }

    public String getShortTypeName() {
      return shortTypeName;
    }

    public String getTypeName() {
      return typeName;
    }

    public String toString() {
      return "code:" + this.code + ", shortTypeName:" + this.shortTypeName + " typeName:" + this.typeName;
    }

  }

  private static Map<Integer, JDBCType> mapByCode = new HashMap<Integer, JDBCType>();
  private static Map<String, JDBCType> mapByTypeName = new HashMap<String, JDBCType>();
  private static Map<String, JDBCType> mapByShortTypeName = new HashMap<String, JDBCType>();

  static {
    for (JDBCType t : JDBCType.values()) {
      mapByCode.put(t.getCode(), t);
      mapByTypeName.put(t.getTypeName(), t);
      mapByShortTypeName.put(t.getShortTypeName(), t);
    }

  }

  /*
   * Returns the code for this JDBC type name. Null if the type name does not
   * exist.
   */
  public static Integer nameToCode(final String jdbcTypeName) {
    JDBCType t = mapByTypeName.get(jdbcTypeName);
    if (t != null) {
      return t.getCode();
    }
    t = mapByShortTypeName.get(jdbcTypeName);
    return t == null ? null : t.getCode();
  }

  /*
   * Returns the JDBC type for this name. Accepts short type name (e.g. "NUMERIC")
   * as well as full type name ("java.sql.Types.NUMERIC"). Null if the type name
   * does not exist.
   */
  public static JDBCType nameToType(final String jdbcTypeName) {
    JDBCType t = mapByTypeName.get(jdbcTypeName);
    if (t != null) {
      return t;
    }
    return mapByShortTypeName.get(jdbcTypeName);
  }

  /*
   * Returns the JDBC full type name for this code. Null if the code does not
   * exist.
   */
  public static String codeToName(final int code) {
    JDBCType t = mapByCode.get(code);
    return t == null ? null : t.getTypeName();
  }

  /*
   * Returns the JDBC type for this code. Null if the code does not exist.
   */
  public static JDBCType codeToType(final int code) {
    return mapByCode.get(code);
  }

  /*
   * Returns the short JDBC type name for this code. Null if the code does not
   * exist.
   */
  public static String codeToShortName(final int code) {
    JDBCType t = mapByCode.get(code);
    return t == null ? null : t.getShortTypeName();
  }

}
