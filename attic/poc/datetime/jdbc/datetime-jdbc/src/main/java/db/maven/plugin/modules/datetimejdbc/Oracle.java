package db.maven.plugin.modules.datetimejdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Oracle extends AbstractTest {

  // To change profiles in Eclipse: Properties-->Maven-->Active Maven Profiles

  private static final String JDBC_DRIVER_CLASS = "oracle.jdbc.driver.OracleDriver";
  private static final String JDBC_URL = "jdbc:oracle:thin:@192.168.56.95:1521:orcl";
  private static final String JDBC_USERNAME = "user1";
  private static final String JDBC_PASSWORD = "pass1";

  //  drop table t1;
  //
  //  create table t1 (
  //    id number(6),
  //    local1 date,
  //    local2 timestamp,
  //    world1 timestamp with time zone,
  //    world2 timestamp with local time zone
  //  );

  //=== Using set/getTimestamp( java.sql.Timestamp ) on a DATE column ===
  // - Inserted  #1: 2020-01-02 12:34:56.0 (class java.sql.Timestamp)
  // - Retrieved #1: 2020-01-02 12:34:56.0 (class java.sql.Timestamp)
  //
  //=== Using set/getObject( java.sql.Timestamp ) on a DATE column ===
  // - Inserted  #1: 2020-01-02 12:34:56.0 (class java.sql.Timestamp)
  // - Retrieved #1: 2020-01-02 12:34:56.0 (class java.sql.Timestamp)
  //
  //=== Using set/getObject( java.time.LocalDateTime ) on a DATE column ===
  // - Inserted  #1: 2020-01-02T12:34:56 (class java.time.LocalDateTime)
  // - Retrieved #1: 2020-01-02T12:34:56 (class java.time.LocalDateTime)
  //
  //=== Using set/getTimestamp( java.sql.Timestamp ) on a TIMESTAMP column ===
  // - Inserted  #1: 2020-01-02 12:34:56.0 (class java.sql.Timestamp)
  // - Retrieved #1: 2020-01-02 12:34:56.0 (class java.sql.Timestamp)
  //
  //=== Using set/getObject( java.time.LocalDateTime ) on a TIMESTAMP column ===
  // - Inserted  #1: 2020-01-02T12:34:56 (class java.time.LocalDateTime)
  // - Retrieved #1: 2020-01-02T12:34:56 (class java.time.LocalDateTime)
  //
  //=== Using set/getObject( java.sql.Timestamp ) on a TIMESTAMP column ===
  // - Inserted  #1: 2020-01-02 12:34:56.0 (class java.sql.Timestamp)
  // - Retrieved #1: 2020-01-02 12:34:56.0 (class java.sql.Timestamp)
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
  // - Retrieved #1: 2020-01-02T12:34:56-03:45 (class java.time.OffsetDateTime)
  //
  //=== Using set/getObject( java.time.ZonedDateTime ) on a TIMESTAMP WITH TIME ZONE column ===
  // - Inserted  #1: 2020-01-02T12:34:56-05:00[America/New_York] (class java.time.ZonedDateTime)
  // - Inserted  #2: 2020-01-02T12:34:56+03:00[Africa/Nairobi] (class java.time.ZonedDateTime)
  // - Inserted  #3: 2020-01-02T12:34:56+01:00[UTC+01:00] (class java.time.ZonedDateTime)
  // - Retrieved #1: 2020-01-02T12:34:56-05:00[America/New_York] (class java.time.ZonedDateTime)
  // - Retrieved #2: 2020-01-02T12:34:56+03:00[Africa/Nairobi] (class java.time.ZonedDateTime)
  // - Retrieved #3: 2020-01-02T12:34:56+01:00 (class java.time.ZonedDateTime)
  //
  //=== Using set/getTimestamp( java.sql.Timestamp ) on a TIMESTAMP WITH LOCAL TIME ZONE column ===
  // - Inserted  #1: 2020-01-02 12:34:56.0 (class java.sql.Timestamp)
  // - Retrieved #1: 2020-01-02 12:34:56.0 (class java.sql.Timestamp)
  //
  //=== Using set/getObject( java.sql.Timestamp ) on a TIMESTAMP WITH LOCAL TIME ZONE column ===
  // - Inserted  #1: 2020-01-02 12:34:56.0 (class java.sql.Timestamp)
  // - Retrieved #1: 2020-01-02 12:34:56.0 (class java.sql.Timestamp)
  //
  //=== Using set/getObject( java.time.OffsetDateTime ) on a TIMESTAMP WITH LOCAL TIME ZONE column ===
  // - Inserted  #1: 2020-01-02T12:34:56-03:45 (class java.time.OffsetDateTime)
  // - Retrieved #1: 2020-01-02T16:19:56Z (class java.time.OffsetDateTime)
  //
  //=== Using set/getObject( java.time.ZonedDateTime ) on a TIMESTAMP WITH LOCAL TIME ZONE column ===
  // - Inserted  #1: 2020-01-02T12:34:56-05:00[America/New_York] (class java.time.ZonedDateTime)
  // - Inserted  #2: 2020-01-02T12:34:56+03:00[Africa/Nairobi] (class java.time.ZonedDateTime)
  // - Inserted  #3: 2020-01-02T12:34:56+01:00[UTC+01:00] (class java.time.ZonedDateTime)
  // - Retrieved #1: 2020-01-02T17:34:56Z (class java.time.ZonedDateTime)
  // - Retrieved #2: 2020-01-02T09:34:56Z (class java.time.ZonedDateTime)
  // - Retrieved #3: 2020-01-02T11:34:56Z (class java.time.ZonedDateTime)

  public static void main(final String[] args) throws ClassNotFoundException, SQLException {

    Class.forName(JDBC_DRIVER_CLASS);
    Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
    new Oracle().testPersistence(conn);
    conn.close();
  }

  private void testPersistence(final Connection conn) throws SQLException {

    // DATE
    persistTimestamp(conn, "DATE", "java.sql.Timestamp", "local1", LIST_TIMESTAMP);
    persistObject(conn, "DATE", "java.sql.Timestamp", "local1", LIST_TIMESTAMP);
    persistObject(conn, "DATE", "java.time.LocalDateTime", "local1", LIST_LOCALDATETIME);

    // TIMESTAMP
    persistTimestamp(conn, "TIMESTAMP", "java.sql.Timestamp", "local2", LIST_TIMESTAMP);
    persistObject(conn, "TIMESTAMP", "java.time.LocalDateTime", "local2", LIST_LOCALDATETIME);
    persistObject(conn, "TIMESTAMP", "java.sql.Timestamp", "local2", LIST_TIMESTAMP);

    // TIMESTAMP WITH TIME ZONE
    persistTimestamp(conn, "TIMESTAMP WITH TIME ZONE", "java.sql.Timestamp", "world1", LIST_TIMESTAMP);
    persistObject(conn, "TIMESTAMP WITH TIME ZONE", "java.sql.Timestamp", "world1", LIST_TIMESTAMP);
    persistObject(conn, "TIMESTAMP WITH TIME ZONE", "java.time.OffsetDateTime", "world1", LIST_OFFSETDATETIME);
    persistObject(conn, "TIMESTAMP WITH TIME ZONE", "java.time.ZonedDateTime", "world1", LIST_ZONEDDATETIME);

    // TIMESTAMP WITH LOCAL TIME ZONE
    persistTimestamp(conn, "TIMESTAMP WITH LOCAL TIME ZONE", "java.sql.Timestamp", "world2", LIST_TIMESTAMP);
    persistObject(conn, "TIMESTAMP WITH LOCAL TIME ZONE", "java.sql.Timestamp", "world2", LIST_TIMESTAMP);
    persistObject(conn, "TIMESTAMP WITH LOCAL TIME ZONE", "java.time.OffsetDateTime", "world2", LIST_OFFSETDATETIME);
    persistObject(conn, "TIMESTAMP WITH LOCAL TIME ZONE", "java.time.ZonedDateTime", "world2", LIST_ZONEDDATETIME);

  }

}
