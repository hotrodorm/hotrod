package app.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public class Types2 {

  public static void main(String[] args) throws SQLException {

    System.out.println("-----------");

    Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.56.95:1521:orcl", "user1", "pass1");
    Statement st = conn.createStatement();
    ResultSet rs = st.executeQuery("select * from user1.types_date_time");
    while (rs.next()) {
      Object oid = rs.getObject(1);
      Object odat1 = rs.getObject(2);
      Object odat2 = rs.getObject(3);
      Object odat3 = rs.getObject(4);
      Object odat4 = rs.getObject(5);
      System.out.println("--- getObject(n) --------");
      System.out.println("oid=" + renderValue(oid));
      System.out.println("odat1=" + renderValue(odat1));
      System.out.println("odat2=" + renderValue(odat2));
      System.out.println("odat3=" + renderValue(odat3));
      System.out.println("odat4=" + renderValue(odat4));

      oid = rs.getObject(1, Integer.class);
      odat1 = rs.getObject(2, LocalDateTime.class);
      odat2 = rs.getObject(3, LocalDateTime.class);
      odat3 = rs.getObject(4, OffsetDateTime.class);
      odat4 = rs.getObject(5, OffsetDateTime.class);
      System.out.println("--- getObject(n, class) --------");
      System.out.println("oid=" + renderValue(oid));
      System.out.println("odat1=" + renderValue(odat1));
      System.out.println("odat2=" + renderValue(odat2));
      System.out.println("odat3=" + renderValue(odat3));
      System.out.println("odat4=" + renderValue(odat4));

      Integer id = rs.getInt(1);
      Timestamp dat1 = rs.getTimestamp(2);
      Timestamp dat2 = rs.getTimestamp(3);
      Timestamp dat3 = rs.getTimestamp(4);
      Timestamp dat4 = rs.getTimestamp(5);
      System.out.println("--- getTimestamp(n) --------");
      System.out.println("dat1=" + dat1);
      System.out.println("dat2=" + dat2);
      System.out.println("dat3=" + dat3);
      System.out.println("dat4=" + dat4);

    }

  }

  // CRUD:
//  t:app.daos.TypesDateTimeVO@3b4f1eb
//  - id=1
//  - dat1=Mon Jan 01 12:34:56 EST 2024
//  - dat2=2024-01-01 12:34:56.0
//  - dat3=2024-01-01T12:34:56+03:00
//  - dat4=2024-01-01 04:34:56.0
//  [ Example complete ]

//  --- getObject(n) --------
//  oid=1 (java.math.BigDecimal)
//  odat1=2024-01-01 12:34:56.0 (java.sql.Timestamp)
//  odat2=2024-01-01 12:34:56.0 (oracle.sql.TIMESTAMP)
//  odat3=oracle.sql.TIMESTAMPTZ@439a8f59 (oracle.sql.TIMESTAMPTZ)
//  odat4=oracle.sql.TIMESTAMPLTZ@61861a29 (oracle.sql.TIMESTAMPLTZ)
//  --- getObject(n, class) --------
//  oid=1 (java.lang.Integer)
//  odat1=2024-01-01T12:34:56 (java.time.LocalDateTime)
//  odat2=2024-01-01T12:34:56 (java.time.LocalDateTime)
//  odat3=2024-01-01T12:34:56+03:00 (java.time.OffsetDateTime)
//  odat4=2024-01-01T09:34:56Z (java.time.OffsetDateTime)
//  --- getTimestamp(n) --------
//  dat1=2024-01-01 12:34:56.0
//  dat2=2024-01-01 12:34:56.0
//  dat3=2024-01-01 04:34:56.0
//  dat4=2024-01-01 04:34:56.0

  private static String renderValue(Object obj) {
    return obj + " (" + (obj == null ? "null" : obj.getClass().getName()) + ")";
  }

}
