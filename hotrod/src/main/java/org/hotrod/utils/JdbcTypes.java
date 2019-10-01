package org.hotrod.utils;

import java.util.HashMap;
import java.util.Map;

public class JdbcTypes {

  /*
   * This enum contains all JDBC types available until Java 8. It's used to be
   * able to run in Java 6.
   */
  public static enum JDBCType {

    ARRAY("ARRAY", java.sql.Types.ARRAY), //
    BIGINT("BIGINT", java.sql.Types.BIGINT), //
    BINARY("BINARY", java.sql.Types.BINARY), //

    BIT("BIT", java.sql.Types.BIT), //
    BLOB("BLOB", java.sql.Types.BLOB), //

    BOOLEAN("BOOLEAN", java.sql.Types.BOOLEAN), //
    CHAR("CHAR", java.sql.Types.CHAR), //
    CLOB("CLOB", java.sql.Types.CLOB), //
    DATALINK("DATALINK", java.sql.Types.DATALINK), //
    DATE("DATE", java.sql.Types.DATE), //

    DECIMAL("DECIMAL", java.sql.Types.DECIMAL), //
    DISTINCT("DISTINCT", java.sql.Types.DISTINCT), //
    DOUBLE("DOUBLE", java.sql.Types.DOUBLE), //
    FLOAT("FLOAT", java.sql.Types.FLOAT), //
    INTEGER("INTEGER", java.sql.Types.INTEGER), //

    JAVA_OBJECT("JAVA_OBJECT", java.sql.Types.JAVA_OBJECT), //
    LONGNVARCHAR("LONGNVARCHAR", java.sql.Types.LONGNVARCHAR), //
    LONGVARBINARY("LONGVARBINARY", java.sql.Types.LONGVARBINARY), //
    LONGVARCHAR("LONGVARCHAR", java.sql.Types.LONGVARCHAR), //
    NCHAR("NCHAR", java.sql.Types.NCHAR), //

    NCLOB("NCLOB", java.sql.Types.NCLOB), //
    NULL("NULL", java.sql.Types.NULL), //
    NUMERIC("NUMERIC", java.sql.Types.NUMERIC), //
    NVARCHAR("NVARCHAR", java.sql.Types.NVARCHAR), //
    OTHER("OTHER", java.sql.Types.OTHER), //

    REAL("REAL", java.sql.Types.REAL), //
    REF("REF", java.sql.Types.REF), //
    REF_CURSOR("REF_CURSOR", 2012), // Available since Java 8
    ROWID("ROWID", java.sql.Types.ROWID), //
    SMALLINT("SMALLINT", java.sql.Types.SMALLINT), //
    SQLXML("SQLXML", java.sql.Types.SQLXML), //

    STRUCT("STRUCT", java.sql.Types.STRUCT), //
    TIME("TIME", java.sql.Types.TIME), //
    TIME_WITH_TIMEZONE("TIME_WITH_TIMEZONE", 2013), // Available since Java 8
    TIMESTAMP("TIMESTAMP", java.sql.Types.TIMESTAMP), //
    TIMESTAMP_WITH_TIMEZONE("TIMESTAMP_WITH_TIMEZONE", 2014), // Available since
                                                              // Java 8
    TINYINT("TINYINT", java.sql.Types.TINYINT), //
    VARBINARY("VARBINARY", java.sql.Types.VARBINARY), //

    VARCHAR("VARCHAR", java.sql.Types.VARCHAR);

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
   * Returns the JDBC type for this name. Accepts short type name (e.g.
   * "NUMERIC") as well as full type name ("java.sql.Types.NUMERIC"). Null if
   * the type name does not exist.
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
