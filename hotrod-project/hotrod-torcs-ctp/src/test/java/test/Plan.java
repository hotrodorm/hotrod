package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Plan {

  public static void main(String[] args) throws SQLException {
    System.out.println("Test 1 -- start");
    Connection conn = DriverManager.getConnection("jdbc:postgresql://192.168.56.214:5432/hotrod", "user1", "pass1");
    PreparedStatement ps1 = conn.prepareStatement("prepare x as select * from documents where content = $1");
    ps1.execute();
    PreparedStatement ps2 = conn.prepareStatement("explain (format json) execute x (?)");
    ps2.setString(1, "long content...");
    ResultSet rs = ps2.executeQuery();
    while (rs.next()) {
      System.out.println(rs.getString(1));
    }
    System.out.println("Test 1 -- end");
  }

}
