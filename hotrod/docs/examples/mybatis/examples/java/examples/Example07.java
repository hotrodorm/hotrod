package examples;

import java.sql.SQLException;

import daos.ClientDAO;
import daos.VehicleDAO;

/**
 * Example 07 - Select by Unique Indexes
 * 
 * @author valarcon
 * 
 */
public class Example07 {

  public static void main(String[] args) throws SQLException {

    System.out.println("=== Running Example 07 - Select by Unique Indexes ===");

    // 1. Select by Unique Index - Single Column
    // Example: Find the vehicle with VIN = VIN7492

    VehicleDAO v1 = VehicleDAO.selectByUIVin("VIN7492");
    if (v1 == null) {
      System.out.println("1. Select by Unique Index - Single Column - Vehicle not found.");
    } else {
      System.out.println("1. Select by Unique Index - Single Column - ID=" + v1.getId());
    }

    // 2. Select by Unique Index - Single Column
    // Example: Find the vehicle with ENGINE_NUMBER = EN102937

    VehicleDAO v2 = VehicleDAO.selectByUIEngineNumber("EN102937");
    if (v2 == null) {
      System.out.println("2. Select by Unique Index - Single Column - Vehicle not found.");
    } else {
      System.out.println("2. Select by Unique Index - Single Column - ID=" + v2.getId());
    }

    // 3. Select by Unique Index - Multiple Columns
    // Example: Find the client from California state, and driver's license
    // C535893758

    ClientDAO c1 = ClientDAO.selectByUIStateDriversLicense("CA", "C535893758");
    if (c1 == null) {
      System.out.println("3. Select by Unique Index - Multiple Columns - Client not found.");
    } else {
      System.out.println("3. Select by Unique Index - Multiple Columns - Client: " + c1.getName());
    }

    System.out.println(" ");
    System.out.println("=== Example 07 Complete ===");

  }

}
