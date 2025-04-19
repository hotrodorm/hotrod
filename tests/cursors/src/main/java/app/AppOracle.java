package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

public class AppOracle {

  private static final String URL = "jdbc:oracle:thin:@192.168.56.95:1521:orcl";
  private static final String USERNAME = "user1";
  private static final String PASSWORD = "pass1";
  
//  private static final int ROWS = 5_000;
  private static final int ROWS = 1_000_000;

  private static final DecimalFormat DF = new DecimalFormat("#,##0");

  public static void main(final String[] args) throws SQLException {
    Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
//    conn.setAutoCommit(false);

    long start = System.currentTimeMillis();

    System.out.println("# Step 1");
    PreparedStatement ps = conn.prepareStatement(
        "select column_value as id, rpad('a', 1000, substr(column_value, -1) || 'a') as name from table(generate_series(1, 200000))");
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
