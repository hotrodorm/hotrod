package examples;

import java.sql.SQLException;
import java.util.List;

import daos.NonVIPClientVO;
import daos.VehiclePriceVO;
import daos.primitives.GeneralOperations;
import daos.primitives.NonVIPClient;
import daos.primitives.VehiclePrice;

/**
 * Example 13 - Native SQL
 * 
 * @author Vladimir Alarcon
 * 
 */
public class Example13 {

  public static void main(String[] args) throws SQLException {

    System.out.println("=== Running Example 13 - Native SQL ===");

    // 1. Limiting the Number of Rows at the DB Level
    // Example: retrieve non-VIP clients, with a maximum of 4 returned rows

    List<NonVIPClientVO> nv = NonVIPClient.select(4);
    System.out.println(" ");
    System.out.println("1. Limiting the Number of Rows at the DB Level - rows=" + nv.size());
    for (NonVIPClientVO c : nv) {
      System.out.println(" client ID=" + c.getId());
    }

    // 2. Using a Native SQL Function
    // Example: using the IFNULL non-standard function

    List<VehiclePriceVO> vp = VehiclePrice.select("Yamaha");
    System.out.println(" ");
    System.out.println("2. Using a Native SQL Function - rows=" + vp.size());
    for (VehiclePriceVO v : vp) {
      System.out.println(" vehicle ID=" + v.getId() + "  price=" + v.getPrice());
    }

    // 3. Inserting on a Native Column Type

    int rows = GeneralOperations.insertPreferredColors(1, "orange", "indigo", "turquoise");
    System.out.println(" ");
    System.out.println("3. Inserting on a Native Column Type - Affected rows=" + rows);

    // Can also be used to:
    // * select for update
    // * recursive selects

    System.out.println(" ");
    System.out.println("=== Example 13 Complete ===");

  }

}
