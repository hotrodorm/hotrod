package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

public class AppSQLServer {

  private static final String URL = "jdbc:sqlserver://192.168.56.51:1433;encrypt=true;trustServerCertificate=true";
  private static final String USERNAME = "admin";
  private static final String PASSWORD = "admin";

  private static final String SQL = "with digit as (\n" //
      + "  select 0 as d union all select 1 union all select 2 union all select 3 union all select 4\n" //
      + "  union all select 5 union all select 6 union all select 7 union all select 8 union all select 9        \n" //
      + "),\n" //
      + "s as (\n" //
      + "  select a.d + 10 * b.d + 100 * c.d + 1000 * d.d + 10000 * e.d as id\n" //
      + "  from digit a cross join digit b cross join digit c cross join digit d cross join digit e\n" //
      + ")\n" //
      + "select id, replicate(concat('a', right(id, 1)), 1000) as title from s";

  private static final DecimalFormat DF = new DecimalFormat("#,##0");

  public static void main(final String[] args) throws SQLException {
    Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

//    conn.setAutoCommit(false);

    long start = System.currentTimeMillis();

    System.out.println("# Step 1");
    PreparedStatement ps = conn.prepareStatement(SQL);
    System.out.println("# Step 2");

//    ps.setFetchSize(100);

    ResultSet rs = ps.executeQuery();
    System.out.println("# Step 3");
    int count = 0;
    int i = 0;

    System.out.println("--- Starting...");

    while (rs.next()) {
      count++;
      i++;
      if (i >= 10000) {
        i = 0;
        System.out.println("- " + count + " - Free: " + DF.format(Runtime.getRuntime().freeMemory()) + " bytes");
      }
    }
    System.out.println("Total rows: " + count + " -- elapsed: " + (System.currentTimeMillis() - start) + " ms");

  }

}
