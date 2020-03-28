package db.maven.plugin.modules.datetimejdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL extends AbstractTest {

  // To change profiles in Eclipse: Properties-->Maven-->Active Maven Profiles

  private static final String JDBC_DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";
  private static final String JDBC_URL = "jdbc:mysql://192.168.56.29:3306/hotrod?useUnicode=true&useSSL=false";
  private static final String JDBC_USERNAME = "user1";
  private static final String JDBC_PASSWORD = "pass1";

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
  //ERROR MysqlDataTruncation: Data truncation: Incorrect datetime value: '\xAC\xED\x00\x05sr\x00\x0Djava.time.Ser\x95]\x84\xBA\x1B"H\xB2\x0C\x00\x00xpw\x0B\x0A\x00\x00\x07\xE4\x01\x02\x0C"\xC7\xF1x' for column 'world1' at row 1
  //
  //=== Using set/getObject( java.time.ZonedDateTime ) on a TIMESTAMP column ===
  //ERROR MysqlDataTruncation: Data truncation: Incorrect datetime value: '\xAC\xED\x00\x05sr\x00\x0Djava.time.Ser\x95]\x84\xBA\x1B"H\xB2\x0C\x00\x00xpw\x1E\x06\x00\x00\x07\xE4\x01\x02\x0C"\xC7\xEC\x07\x' for column 'world1' at row 1

  public static void main(final String[] args) throws ClassNotFoundException, SQLException {

    Class.forName(JDBC_DRIVER_CLASS);
    Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
    new MySQL().testPersistence(conn);
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
