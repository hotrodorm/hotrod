package db.maven.plugin.modules.datetimejdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MariaDB extends AbstractTest {

  // To change profiles in Eclipse: Properties-->Maven-->Active Maven Profiles

  private static final String JDBC_DRIVER_CLASS = "org.mariadb.jdbc.Driver";
  private static final String JDBC_URL = "jdbc:mariadb://192.168.56.205:3306/database1";
  private static final String JDBC_USERNAME = "my_user";
  private static final String JDBC_PASSWORD = "mypass";

  //  drop table t1;
  //
  //  create table t1 (
  //    id int,
  //    local1 datetime,
  //    world1 timestamp
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
  //=== Using set/getTimestamp( java.sql.Timestamp ) on a TIMESTAMP column ===
  // - Inserted  #1: 2020-01-02 12:34:56.0 (class java.sql.Timestamp)
  // - Retrieved #1: 2020-01-02 12:34:56.0 (class java.sql.Timestamp)
  //
  //=== Using set/getObject( java.sql.Timestamp ) on a TIMESTAMP column ===
  // - Inserted  #1: 2020-01-02 12:34:56.0 (class java.sql.Timestamp)
  // - Retrieved #1: 2020-01-02 12:34:56.0 (class java.sql.Timestamp)
  //
  //=== Using set/getObject( java.time.OffsetDateTime ) on a TIMESTAMP column ===
  // - Inserted  #1: 2020-01-02T12:34:56-03:45 (class java.time.OffsetDateTime)
  // - Retrieved #1: 2020-01-02T11:19:56-05:00 (class java.time.OffsetDateTime)
  //
  //=== Using set/getObject( java.time.ZonedDateTime ) on a TIMESTAMP column ===
  // - Inserted  #1: 2020-01-02T12:34:56-05:00[America/New_York] (class java.time.ZonedDateTime)
  // - Inserted  #2: 2020-01-02T12:34:56+03:00[Africa/Nairobi] (class java.time.ZonedDateTime)
  // - Inserted  #3: 2020-01-02T12:34:56+01:00[UTC+01:00] (class java.time.ZonedDateTime)
  // - Retrieved #1: 2020-01-02T12:34:56-05:00[America/New_York] (class java.time.ZonedDateTime)
  // - Retrieved #2: 2020-01-02T04:34:56-05:00[America/New_York] (class java.time.ZonedDateTime)
  // - Retrieved #3: 2020-01-02T06:34:56-05:00[America/New_York] (class java.time.ZonedDateTime)

  public static void main(final String[] args) throws ClassNotFoundException, SQLException {

    Class.forName(JDBC_DRIVER_CLASS);
    Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
    new MariaDB().testPersistence(conn);
    conn.close();
  }

  private void testPersistence(final Connection conn) throws SQLException {

    // DATETIME
    persistTimestamp(conn, "DATETIME", "java.sql.Timestamp", "local1", LIST_TIMESTAMP);
    persistObject(conn, "DATETIME", "java.sql.Timestamp", "local1", LIST_TIMESTAMP);
    persistObject(conn, "DATETIME", "java.time.LocalDateTime", "local1", LIST_LOCALDATETIME);

    // TIMESTAMP
    persistTimestamp(conn, "TIMESTAMP", "java.sql.Timestamp", "world1", LIST_TIMESTAMP);
    persistObject(conn, "TIMESTAMP", "java.sql.Timestamp", "world1", LIST_TIMESTAMP);
    persistObject(conn, "TIMESTAMP", "java.time.OffsetDateTime", "world1", LIST_OFFSETDATETIME);
    persistObject(conn, "TIMESTAMP", "java.time.ZonedDateTime", "world1", LIST_ZONEDDATETIME);

  }

}
