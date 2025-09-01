package examples.crud2;

import java.sql.SQLException;

import daos.ClientVO;
import daos.VehicleVO;
import daos.primitives.ClientDAO;
import daos.primitives.VehicleDAO;

/**
 * Example CRUD 205 - Select By Unique Constraint
 * 
 * @author Vladimir Alarcon
 * 
 */
public class ExampleCrud205 {

  public static void main(String[] args) throws SQLException {

    // 1. Select by Unique Index - Single Column
    // Example: Find the vehicle with VIN = VIN7492

//    VehicleVO v1 = VehicleDAO.selectByUIVin("VIN7492");
//    if (v1 == null) {
//      System.out.println("1. Select by Unique Index (single column) - Vehicle not found.");
//    } else {
//      System.out.println("1. Select by Unique Index (single column) - ID=" + v1.getId());
//    }

    // 2. Select by Unique Index - Single Column
    // Example: Find the vehicle with ENGINE_NUMBER = EN102937

//    VehicleVO v2 = VehicleDAO.selectByUIEngineNumber("EN102937");
//    if (v2 == null) {
//      System.out.println("2. Select by Unique Index (single column) - Vehicle not found.");
//    } else {
//      System.out.println("2. Select by Unique Index (single column) - ID=" + v2.getId());
//    }

    // 3. Select by Unique Composite Index
    // Example: Find the client from California state, and driver's license
    // C535893758

    ClientVO c1 = ClientDAO.selectByUIStateDriversLicense("CA", "C535893758");
    if (c1 == null) {
      System.out.println("3. Select by Unique Composite Index - Client not found.");
    } else {
      System.out.println("3. Select by Unique Composite Index - Client: " + c1.getName());
    }

  }

}
