package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PGPlanTest {

  public static void main(final String[] args) throws SQLException, ClassNotFoundException {
    System.out.println("--- Start ---");
//    Class.forName("org.postgresql.Driver");
    Connection conn = DriverManager.getConnection("jdbc:postgresql://192.168.56.214:5432/hotrod", "user1", "pass1");

    PreparedStatement ps1 = conn.prepareStatement("prepare plan1 (unknown) as " //
        + "SELECT i.id "
//        + ", i.amount, i.branch_id, b.id as bid, b.\"NaMe\"  " //
        + "FROM public.invoice i JOIN public.branch b ON b.id = i.branch_id WHERE b.\"NaMe\" like $1");
    ps1.execute();

    PreparedStatement ps2 = conn.prepareStatement("explain (format json) execute plan1 (?)");

    ps2.setString(1, "a%");

    ResultSet rs = ps2.executeQuery();

    while (rs.next()) {
      String line = rs.getString(1);
      System.out.println("> " + line);
    }
    System.out.println("---");
  }

}
