package research.dmlparameters;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class App {

  public static void main(String[] args) throws SQLException {

    String url = "jdbc:postgresql://192.168.56.214:5432/hotrod";
    String username = "user1";
    String password = "pass1";

    Connection conn = DriverManager.getConnection(url, username, password);

//    run1(conn);
    run2(conn);

  }

  private static void run2(Connection conn) throws SQLException {
    PreparedStatement ps = conn.prepareStatement(
        "create or replace view hotrodtempview101 as " +
         "select * from product where price between null and 5");
//    ps.setInt(1, 0);
ps.execute();
//    ResultSet rs = ps.executeQuery();
//    while (rs.next()) {
//      int id = rs.getInt(1);
//      System.out.println("id=" + id);
//    }
  }

  private static void run1(Connection conn) throws SQLException {
    PreparedStatement ps = conn.prepareStatement("select * from product where price > ?");
    ps.setInt(1, 5);

    ResultSet rs = ps.executeQuery();
    while (rs.next()) {
      int id = rs.getInt(1);
      System.out.println("id=" + id);
    }
  }

}
