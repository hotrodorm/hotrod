package sql.expressions;

import java.sql.Types;

public enum JDBCType {

  BIT(Types.BIT), // -7
  TINYINT(Types.TINYINT), // -6
  SMALLINT(Types.SMALLINT), // 5
  INTEGER(Types.INTEGER), // 4
  BIGINT(Types.BIGINT), // -5
  FLOAT(Types.FLOAT), // 6
  REAL(Types.REAL), // 7
  DOUBLE(Types.DOUBLE), // 8
  NUMERIC(Types.NUMERIC), // 2
  DECIMAL(Types.DECIMAL), // 3
  CHAR(Types.CHAR), // 1
  VARCHAR(Types.VARCHAR), // 12
  LONGVARCHAR(Types.LONGVARCHAR), // -1
  DATE(Types.DATE), // 91
  TIME(Types.TIME), // 92
  TIMESTAMP(Types.TIMESTAMP), // 93
  BINARY(Types.BINARY), // -2
  VARBINARY(Types.VARBINARY), // -3
  LONGVARBINARY(Types.LONGVARBINARY), // -4
  NULL(Types.NULL), // 0
  OTHER(Types.OTHER), // 1111
  JAVA_OBJECT(Types.JAVA_OBJECT), // 2000
  DISTINCT(Types.DISTINCT), // 2001
  STRUCT(Types.STRUCT), // 2002
  ARRAY(Types.ARRAY), // 2003
  BLOB(Types.BLOB), // 2004
  CLOB(Types.CLOB), // 2005
  REF(Types.REF), // 2006
  DATALINK(Types.DATALINK), // 70
  BOOLEAN(Types.BOOLEAN), // 16

  // JDBC 4.0 (Java 6)

  ROWID(-8), //
  NCHAR(-15), //
  NVARCHAR(-9), //
  LONGNVARCHAR(-16), //
  NCLOB(2011), //
  SQLXML(2009), //

  // JDCB 4.2 (Java 8)

  REF_CURSOR(2012), //
  TIME_WITH_TIMEZONE(2013), //
  TIMESTAMP_WITH_TIMEZONE(2014); //

  private Integer code;

  private JDBCType(final Integer code) {
    this.code = code;
  }

  public Integer getCode() {
    return this.code;
  }

}
