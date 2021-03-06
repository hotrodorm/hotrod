package examples;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import daos.ClientNeverOfferedDiscountVO;
import daos.ClientWithPurchaseVO;
import daos.CreatedClientVO;
import daos.DailyTotalVO;
import daos.primitives.ClientNeverOfferedDiscount;
import daos.primitives.ClientWithPurchase;
import daos.primitives.CreatedClient;
import daos.primitives.DailyTotal;

/**
 * Example 12 - Regular SQL Selects
 * 
 * @author Vladimir Alarcon
 * 
 */
public class Example12 {

  public static void main(String[] args) throws SQLException {

    System.out.println("=== Running Example 12 - Regular SQL Selects ===");

    // 1. Simple Select
    // Example: retrieve clients created between two dates

    Date since = Date.valueOf("2017-01-01");
    Date until = Date.valueOf("2017-03-31");
    List<CreatedClientVO> c1 = CreatedClient.select(since, until);
    System.out.println(" ");
    System.out.println("1. Simple Select - rows=" + c1.size());
    for (CreatedClientVO c : c1) {
      System.out.println(" client ID=" + c.getId());
    }

    // 2. Select Returns Fully-Typed Columns From Multiple Tables
    // Example: clients with purchases on a specific date

    Date today = Date.valueOf("2017-02-28");
    List<ClientWithPurchaseVO> c2 = ClientWithPurchase.select(today);
    System.out.println(" ");
    System.out.println("2. Select Returns Fully-Typed Columns From Multiple Tables - rows=" + c2.size());
    for (ClientWithPurchaseVO c : c2) {
      System.out.println(" client ID=" + c.getId());
    }

    // 3. Select Grouping Data Adding Counter
    // Example: Compute Daily Totals

    Date from = Date.valueOf("2017-01-01");
    Date to = Date.valueOf("2017-03-31");
    List<DailyTotalVO> totals = DailyTotal.select(from, to);
    System.out.println(" ");
    System.out.println("3. Select Grouping Data Adding Counter - rows=" + totals.size());
    for (DailyTotalVO dt : totals) {
      System.out.println(" Date: " + dt.getPurchaseDate() + " - Number of Purchases=" + dt.getNumberOfPurchases()
          + " - Total Revenue=" + dt.getRevenue());
    }

    // 4. Select Using Sub Queries
    // Example: Client Never Offered a Discount

    List<ClientNeverOfferedDiscountVO> c4 = ClientNeverOfferedDiscount.select();
    System.out.println(" ");
    System.out.println("4. Select Using Sub Queries - rows=" + c4.size());
    for (ClientNeverOfferedDiscountVO c : c4) {
      System.out.println(" client ID=" + c.getId());
    }

    System.out.println(" ");
    System.out.println("=== Example 12 Complete ===");

  }

}
