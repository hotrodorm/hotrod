package examples;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import daos.SearchedClientVO;
import daos.primitives.GeneralOperations;
import daos.primitives.SearchedClient;

/**
 * Example 14 - Dynamic SQL
 * 
 * @author Vladimir Alarcon
 * 
 */
public class Example14 {

  public static void main(String[] args) throws SQLException {

    int rows;

    System.out.println("=== Running Example 14 - Dynamic SQL ===");

    // 1. Searching by Combined Conditions

    // Example A: search clients from the VA state, created since 2017-01-13

    Date since = Date.valueOf("2017-01-13");
    List<SearchedClientVO> c1 = SearchedClient.select(null, "VA", since);
    System.out.println(" ");
    System.out.println("1.A. Searching by Combined Conditions (state. since) - rows=" + c1.size());
    for (SearchedClientVO c : c1) {
      System.out.println(" Client ID=" + c.getId());
    }

    // Example B: search clients with a minimum of 1 purchase, created since
    // 2017-01-13

    List<SearchedClientVO> c2 = SearchedClient.select(1, null, since);
    System.out.println(" ");
    System.out.println("1.B. Searching by Combined Conditions (#purchases, since) - rows=" + c2.size());
    for (SearchedClientVO c : c2) {
      System.out.println(" Client ID=" + c.getId());
    }

    // 2. Dynamic SQL - Update by Combined Conditions

    // Example A: Discount $100 on Price of Any Unsold Vehicle with 50000 miles
    // or more

    rows = GeneralOperations.applyDiscountToVehicles(100, true, 50000, null);
    System.out.println(" ");
    System.out.println("2.A. Update by Combined Conditions (unsold, mileage) - rows=" + rows);

    // Example B: Discount $500 on Price on Unsold Trucks with any mileage

    rows = GeneralOperations.applyDiscountToVehicles(500, true, null, "TRUCK");
    System.out.println(" ");
    System.out.println("2.B. Update by Combined Conditions (unsold, type) - rows=" + rows);

    System.out.println(" ");
    System.out.println("=== Example 14 Complete ===");

  }

}
