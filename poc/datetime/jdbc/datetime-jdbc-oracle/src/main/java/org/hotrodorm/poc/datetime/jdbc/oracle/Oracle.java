package org.hotrodorm.poc.datetime.jdbc.oracle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

public class Oracle {

  private static final String JDBC_DRIVER_CLASS = "oracle.jdbc.driver.OracleDriver";
  private static final String JDBC_URL = "jdbc:oracle:thin:@192.168.56.95:1521:orcl";
  private static final String JDBC_USERNAME = "user1";
  private static final String JDBC_PASSWORD = "pass1";

  private static final LocalDateTime LOCAL_DT = LocalDateTime.of(2020, 1, 2, 12, 34, 56);

  private static final List<OffsetDateTime> GLOBAL_OFFSET_DT = Arrays.asList( //
      OffsetDateTime.of(2020, 1, 2, 12, 34, 56, 0, ZoneOffset.ofHoursMinutes(-3, -45)) //
  );

  private static final List<ZonedDateTime> GLOBAL_ZONED_DT = Arrays.asList( //
      ZonedDateTime.of(2020, 1, 2, 12, 34, 56, 0, ZoneId.of("America/New_York")), //
      ZonedDateTime.of(2020, 1, 2, 12, 34, 56, 0, ZoneId.of("Africa/Nairobi")), //
      ZonedDateTime.of(2020, 1, 2, 12, 34, 56, 0, ZoneId.of("UTC+01:00")) //
  );

//  drop table t1;
//
//  create table t1 (
//    id number(6),
//    a timestamp,
//    b timestamp with time zone
//  );  
  
//     === Persist LocalDateTime ===
//      - Inserted #1: 2020-01-02T12:34:56
//      - Retrieved #1: 2020-01-02T12:34:56
//     === Persist OffsetDateTime ===
//      - Inserted #1: 2020-01-02T12:34:56-03:45
//      - Retrieved #1: 2020-01-02T12:34:56-03:45
//     === Persist ZonedDateTime ===
//      - Inserted #1: 2020-01-02T12:34:56-05:00[America/New_York]
//      - Inserted #2: 2020-01-02T12:34:56+03:00[Africa/Nairobi]
//      - Inserted #3: 2020-01-02T12:34:56+01:00[UTC+01:00]
//      - Retrieved #1: 2020-01-02T12:34:56-05:00[America/New_York]
//      - Retrieved #2: 2020-01-02T12:34:56+03:00[Africa/Nairobi]
//      - Retrieved #3: 2020-01-02T12:34:56+01:00

  public static void main(final String[] args) throws ClassNotFoundException, SQLException {
    persistLocalDateTime();
    persistOffsetDateTime();
    persistZonedDateTime();
  }

  private static void persistLocalDateTime() throws ClassNotFoundException, SQLException {

    System.out.println("=== Persist LocalDateTime ===");

    Class.forName(JDBC_DRIVER_CLASS);
    Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);

    {
      PreparedStatement ps = conn.prepareStatement("delete from t1");
      ps.execute();
    }

    {
      PreparedStatement ps = conn.prepareStatement("insert into t1 (id, a) values (?, ?)");
      int id = 1;
      ps.setInt(1, id);
      ps.setObject(2, LOCAL_DT);
      ps.execute();
      ps.close();
      System.out.println(" - Inserted #" + id + ": " + LOCAL_DT);
    }

    {
      PreparedStatement ps = conn.prepareStatement("select id, a from t1 order by id");
      ResultSet rs = ps.executeQuery();
      int rows = 0;
      while (rs.next()) {
        rows++;
        int id = rs.getInt(1);
        LocalDateTime a = rs.getObject(2, LocalDateTime.class);
        System.out.println(" - Retrieved #" + id + ": " + a);
      }
      rs.close();
      ps.close();
    }
  }

  private static void persistOffsetDateTime() throws ClassNotFoundException, SQLException {

    System.out.println("=== Persist OffsetDateTime ===");

    Class.forName(JDBC_DRIVER_CLASS);
    Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);

    {
      PreparedStatement ps = conn.prepareStatement("delete from t1");
      ps.execute();
    }

    {
      PreparedStatement ps = conn.prepareStatement("insert into t1 (id, b) values (?, ?)");
      int id = 1;
      for (OffsetDateTime o : GLOBAL_OFFSET_DT) {
        ps.setInt(1, id);
        ps.setObject(2, o);
        ps.execute();
        System.out.println(" - Inserted #" + id + ": " + o);
        id++;
      }
      ps.close();
    }

    {
      PreparedStatement ps = conn.prepareStatement("select id, b from t1 order by id");
      ResultSet rs = ps.executeQuery();
      int rows = 0;
      while (rs.next()) {
        rows++;
        int id = rs.getInt(1);
        OffsetDateTime b = rs.getObject(2, OffsetDateTime.class);
        System.out.println(" - Retrieved #" + id + ": " + b);
      }
      rs.close();
      ps.close();
    }
  }

  private static void persistZonedDateTime() throws ClassNotFoundException, SQLException {

    System.out.println("=== Persist ZonedDateTime ===");

    Class.forName(JDBC_DRIVER_CLASS);
    Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);

    {
      PreparedStatement ps = conn.prepareStatement("delete from t1");
      ps.execute();
    }

    {
      PreparedStatement ps = conn.prepareStatement("insert into t1 (id, b) values (?, ?)");
      int id = 1;
      for (ZonedDateTime o : GLOBAL_ZONED_DT) {
        ps.setInt(1, id);
        ps.setObject(2, o);
        ps.execute();
        System.out.println(" - Inserted #" + id + ": " + o);
        id++;
      }
      ps.close();
    }

    {
      PreparedStatement ps = conn.prepareStatement("select id, b from t1 order by id");
      ResultSet rs = ps.executeQuery();
      int rows = 0;
      while (rs.next()) {
        rows++;
        int id = rs.getInt(1);
        ZonedDateTime b = rs.getObject(2, ZonedDateTime.class);
        System.out.println(" - Retrieved #" + id + ": " + b);
      }
      rs.close();
      ps.close();
    }
  }

}
