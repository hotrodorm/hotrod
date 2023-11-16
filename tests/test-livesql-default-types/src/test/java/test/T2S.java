package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

public class T2S {

  public static void main(String[] args) throws SQLException {

    Connection conn = DriverManager.getConnection(
        "jdbc:sqlserver://192.168.56.51:1433;encrypt=true;trustServerCertificate=true", "admin", "admin");

    conn.setAutoCommit(false);

//    PreparedStatement ps0 = conn.prepareStatement("set showplan_text on;");
//    boolean more0 = ps0.execute();
//    int ct0 = ps0.getUpdateCount();
//    System.out.println("more0=" + more0 + " ct0=" + ct0);

    Statement ps0 = conn.createStatement();
    boolean more0 = ps0.execute("set showplan_text on");
    int ct0 = ps0.getUpdateCount();
    System.out.println("more0=" + more0 + " ct0=" + ct0);

//    Statement ps7 = conn.createStatement();
//    boolean more7 = ps7.execute("declare @abc varchar");
//    int ct7 = ps7.getUpdateCount();
//    System.out.println("more7=" + more7 + " ct7=" + ct7);

    
//    PreparedStatement ps = conn.prepareStatement("select name from account");
    Statement ps = conn.createStatement();
    boolean more1 = ps.execute("declare @abc varchar; declare @def varchar; select amount_granted + @abc + @def from account");
    int ct1 = ps.getUpdateCount();
    System.out.println("more1=" + more1 + " ct1=" + ct1);
    
    read(ps);

    boolean more2 = ps.getMoreResults();
    int ct2 = ps.getUpdateCount();
    System.out.println("more2=" + more2 + " ct2=" + ct2);
    read(ps);

    boolean more3 = ps.getMoreResults();
    int ct3 = ps.getUpdateCount();
    System.out.println("more3=" + more3 + " ct3=" + ct3);
    read(ps);
  }

  private static void read(Statement ps) throws SQLException {
    ResultSet rs = ps.getResultSet();

    while (rs.next()) {
      String val = rs.getString(1);
      System.out.println("val=" + val);
    }
  }

}
