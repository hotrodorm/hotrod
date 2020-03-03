package db.maven.plugin.modules.datetimejdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLServer extends AbstractTest {

  // To change profiles in Eclipse: Properties-->Maven-->Active Maven Profiles

  private static final String JDBC_DRIVER_CLASS = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
  private static final String JDBC_URL = "jdbc:sqlserver://192.168.56.51:1433";
  private static final String JDBC_USERNAME = "admin";
  private static final String JDBC_PASSWORD = "admin";

  //  drop table t1;
  //
  //  create table t1 (
  //   id int,
  //    local1 datetime,
  //    local2 datetime2,
  //    local3 smalldatetime,
  //    world1 datetimeoffset
  //  );

  //=== Using set/getTimestamp( java.sql.Timestamp ) on a DATETIME column ===
  // - Inserted  #1: 2020-01-02 12:34:56.0 (class java.sql.Timestamp)
  // - Retrieved #1: 2020-01-02 12:34:56.0 (class java.sql.Timestamp)
  //
  //=== Using set/getObject( java.sql.Timestamp ) on a DATETIME column ===
  // - Inserted  #1: 2020-01-02 12:34:56.0 (class java.sql.Timestamp)
  // - Retrieved #1: 2020-01-02 12:34:56.0 (class java.sql.Timestamp)
  //
  //=== Using set/getObject( java.time.LocalDateTime ) on a DATETIME column ===
  // - Inserted  #1: 2020-01-02T12:34:56 (class java.time.LocalDateTime)
  // - Retrieved #1: 2020-01-02T12:34:56 (class java.time.LocalDateTime)
  //
  //=== Using set/getTimestamp( java.sql.Timestamp ) on a DATETIME2 column ===
  // - Inserted  #1: 2020-01-02 12:34:56.0 (class java.sql.Timestamp)
  // - Retrieved #1: 2020-01-02 12:34:56.0 (class java.sql.Timestamp)
  //
  //=== Using set/getObject( java.sql.Timestamp ) on a DATETIME2 column ===
  // - Inserted  #1: 2020-01-02 12:34:56.0 (class java.sql.Timestamp)
  // - Retrieved #1: 2020-01-02 12:34:56.0 (class java.sql.Timestamp)
  //
  //=== Using set/getObject( java.time.LocalDateTime ) on a DATETIME2 column ===
  // - Inserted  #1: 2020-01-02T12:34:56 (class java.time.LocalDateTime)
  // - Retrieved #1: 2020-01-02T12:34:56 (class java.time.LocalDateTime)
  //
  //=== Using set/getTimestamp( java.sql.Timestamp ) on a SMALLDATETIME column ===
  // - Inserted  #1: 2020-01-02 12:34:56.0 (class java.sql.Timestamp)
  // - Retrieved #1: 2020-01-02 12:34:56.0 (class java.sql.Timestamp)
  //
  //=== Using set/getObject( java.sql.Timestamp ) on a SMALLDATETIME column ===
  // - Inserted  #1: 2020-01-02 12:34:56.0 (class java.sql.Timestamp)
  // - Retrieved #1: 2020-01-02 12:34:56.0 (class java.sql.Timestamp)
  //
  //=== Using set/getObject( java.time.LocalDateTime ) on a SMALLDATETIME column ===
  // - Inserted  #1: 2020-01-02T12:34:56 (class java.time.LocalDateTime)
  // - Retrieved #1: 2020-01-02T12:34:56 (class java.time.LocalDateTime)
  //
  //=== Using set/getTimestamp( java.sql.Timestamp ) on a DATETIMEOFFSET column ===
  // - Inserted  #1: 2020-01-02 12:34:56.0 (class java.sql.Timestamp)
  // - Retrieved #1: 2020-01-02 07:34:56.0 (class java.sql.Timestamp)
  //
  //=== Using set/getObject( java.sql.Timestamp ) on a DATETIMEOFFSET column ===
  // - Inserted  #1: 2020-01-02 12:34:56.0 (class java.sql.Timestamp)
  // - Retrieved #1: 2020-01-02 07:34:56.0 (class java.sql.Timestamp)
  //
  //=== Using set/getObject( java.time.OffsetDateTime ) on a DATETIMEOFFSET column ===
  // - Inserted  #1: 2020-01-02T12:34:56-03:45 (class java.time.OffsetDateTime)
  // - Retrieved #1: 2020-01-02T12:34:56-03:45 (class java.time.OffsetDateTime)
  //
  //=== Using set/getObject( java.time.ZonedDateTime ) on a DATETIMEOFFSET column ===
  //ERROR SQLServerException: The conversion from UNKNOWN to UNKNOWN is unsupported.

  public static void main(final String[] args) throws ClassNotFoundException, SQLException {

    Class.forName(JDBC_DRIVER_CLASS);
    Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
    new SQLServer().testPersistence(conn);
    conn.close();
  }

  private void testPersistence(final Connection conn) throws SQLException {

    // DATETIME
    persistTimestamp(conn, "DATETIME", "java.sql.Timestamp", "local1", LIST_TIMESTAMP);
    persistObject(conn, "DATETIME", "java.sql.Timestamp", "local1", LIST_TIMESTAMP);
    persistObject(conn, "DATETIME", "java.time.LocalDateTime", "local1", LIST_LOCALDATETIME);

    // DATETIME2
    persistTimestamp(conn, "DATETIME2", "java.sql.Timestamp", "local2", LIST_TIMESTAMP);
    persistObject(conn, "DATETIME2", "java.sql.Timestamp", "local2", LIST_TIMESTAMP);
    persistObject(conn, "DATETIME2", "java.time.LocalDateTime", "local2", LIST_LOCALDATETIME);

    // SMALLDATETIME
    persistTimestamp(conn, "SMALLDATETIME", "java.sql.Timestamp", "local2", LIST_TIMESTAMP);
    persistObject(conn, "SMALLDATETIME", "java.sql.Timestamp", "local2", LIST_TIMESTAMP);
    persistObject(conn, "SMALLDATETIME", "java.time.LocalDateTime", "local2", LIST_LOCALDATETIME);

    // DATETIMEOFFSET
    persistTimestamp(conn, "DATETIMEOFFSET", "java.sql.Timestamp", "world1", LIST_TIMESTAMP);
    persistObject(conn, "DATETIMEOFFSET", "java.sql.Timestamp", "world1", LIST_TIMESTAMP);
    persistObject(conn, "DATETIMEOFFSET", "java.time.OffsetDateTime", "world1", LIST_OFFSETDATETIME);
    persistObject(conn, "DATETIMEOFFSET", "java.time.ZonedDateTime", "world1", LIST_ZONEDDATETIME);

  }

}
