package db.maven.plugin.modules.datetimejdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgreSQL extends AbstractTest {

  // To change profiles in Eclipse: Properties-->Maven-->Active Maven Profiles

  private static final String JDBC_DRIVER_CLASS = "org.postgresql.Driver";
  private static final String JDBC_URL = "jdbc:postgresql://192.168.56.213:5432/postgres";
  private static final String JDBC_USERNAME = "postgres";
  private static final String JDBC_PASSWORD = "mypassword";

  //  drop table t1;
  //
  //  create table t1 (
  //    id int,
  //    local1 timestamp,
  //    world1 timestamp with time zone
  //  );

  //=== Using set/getTimestamp( java.sql.Timestamp ) on a TIMESTAMP column ===
  // - Inserted  #1: 2020-01-02 12:34:56.0 (class java.sql.Timestamp)
  // - Retrieved #1: 2020-01-02 12:34:56.0 (class java.sql.Timestamp)
  //
  //=== Using set/getObject( java.sql.Timestamp ) on a TIMESTAMP column ===
  // - Inserted  #1: 2020-01-02 12:34:56.0 (class java.sql.Timestamp)
  // - Retrieved #1: 2020-01-02 12:34:56.0 (class java.sql.Timestamp)
  //
  //=== Using set/getObject( java.time.LocalDateTime ) on a TIMESTAMP column ===
  // - Inserted  #1: 2020-01-02T12:34:56 (class java.time.LocalDateTime)
  // - Retrieved #1: 2020-01-02T12:34:56 (class java.time.LocalDateTime)
  //
  //=== Using set/getTimestamp( java.sql.Timestamp ) on a TIMESTAMP WITH TIME ZONE column ===
  // - Inserted  #1: 2020-01-02 12:34:56.0 (class java.sql.Timestamp)
  // - Retrieved #1: 2020-01-02 12:34:56.0 (class java.sql.Timestamp)
  //
  //=== Using set/getObject( java.sql.Timestamp ) on a TIMESTAMP WITH TIME ZONE column ===
  // - Inserted  #1: 2020-01-02 12:34:56.0 (class java.sql.Timestamp)
  // - Retrieved #1: 2020-01-02 12:34:56.0 (class java.sql.Timestamp)
  //
  //=== Using set/getObject( java.time.OffsetDateTime ) on a TIMESTAMP WITH TIME ZONE column ===
  // - Inserted  #1: 2020-01-02T12:34:56-03:45 (class java.time.OffsetDateTime)
  // - Retrieved #1: 2020-01-02T16:19:56Z (class java.time.OffsetDateTime)
  //
  //=== Using set/getObject( java.time.ZonedDateTime ) on a TIMESTAMP WITH TIME ZONE column ===
  //ERROR PSQLException: Can't infer the SQL type to use for an instance of java.time.ZonedDateTime. Use setObject() with an explicit Types value to specify the type to use.

  public static void main(final String[] args) throws ClassNotFoundException, SQLException {

    Class.forName(JDBC_DRIVER_CLASS);
    Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
    new PostgreSQL().testPersistence(conn);
    conn.close();
  }

  private void testPersistence(final Connection conn) throws SQLException {

    // TIMESTAMP
    persistTimestamp(conn, "TIMESTAMP", "java.sql.Timestamp", "local1", LIST_TIMESTAMP);
    persistObject(conn, "TIMESTAMP", "java.sql.Timestamp", "local1", LIST_TIMESTAMP);
    persistObject(conn, "TIMESTAMP", "java.time.LocalDateTime", "local1", LIST_LOCALDATETIME);

    // TIMESTAMP WITH TIME ZONE
    persistTimestamp(conn, "TIMESTAMP WITH TIME ZONE", "java.sql.Timestamp", "world1", LIST_TIMESTAMP);
    persistObject(conn, "TIMESTAMP WITH TIME ZONE", "java.sql.Timestamp", "world1", LIST_TIMESTAMP);
    persistObject(conn, "TIMESTAMP WITH TIME ZONE", "java.time.OffsetDateTime", "world1", LIST_OFFSETDATETIME);
    persistObject(conn, "TIMESTAMP WITH TIME ZONE", "java.time.ZonedDateTime", "world1", LIST_ZONEDDATETIME);

  }

}
