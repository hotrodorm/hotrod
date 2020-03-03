package db.maven.plugin.modules.datetimejdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB2 extends AbstractTest {

  // To change profiles in Eclipse: Properties-->Maven-->Active Maven Profiles

  private static final String JDBC_DRIVER_CLASS = "com.ibm.db2.jcc.DB2Driver";
  private static final String JDBC_URL = "jdbc:db2://192.168.56.44:50000/empusa";
  private static final String JDBC_USERNAME = "user1";
  private static final String JDBC_PASSWORD = "pass1";

  //  drop table t1;
  //
  //  create table t1 (
  //    id int,
  //    local1 timestamp
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
  //ERROR SqlSyntaxErrorException: [jcc][1091][10824][4.26.14] Invalid data conversion: Parameter instance 2020-01-02T12:34:56 is invalid for the requested conversion. ERRORCODE=-4461, SQLSTATE=42815

  public static void main(final String[] args) throws ClassNotFoundException, SQLException {

    Class.forName(JDBC_DRIVER_CLASS);
    Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
    new DB2().testPersistence(conn);
    conn.close();
  }

  private void testPersistence(final Connection conn) throws SQLException {

    // TIMESTAMP
    persistTimestamp(conn, "TIMESTAMP", "java.sql.Timestamp", "local1", LIST_TIMESTAMP);
    persistObject(conn, "TIMESTAMP", "java.sql.Timestamp", "local1", LIST_TIMESTAMP);
    persistObject(conn, "TIMESTAMP", "java.time.LocalDateTime", "local1", LIST_LOCALDATETIME);

  }

}
