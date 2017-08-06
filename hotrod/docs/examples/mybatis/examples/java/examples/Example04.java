package examples;

import java.sql.Date;
import java.sql.SQLException;

import daos.VehicleVO;
import daos.primitives.VehicleDAO;

/**
 * Example 04 - Update by Example
 * 
 * @author Vladimir Alarcon
 * 
 */
public class Example04 {

  public static void main(String[] args) throws SQLException {

    System.out.println("=== Running Example 04 - Update by Example ===");
    VehicleVO example;
    VehicleVO updateValues;
    int rows;
    Utilities.displayAllVehicles("Initial Data on Table VEHICLE:");

    // 1. Single Column Update
    // Example: Set mileage to 10 to all cars on branch 101.

    example = new VehicleVO();
    example.setBranchId(103); // search branch_id = 103
    updateValues = new VehicleVO();
    updateValues.setMileage(10); // set mileage to 10
    rows = VehicleDAO.updateByExample(example, updateValues);
    Utilities.displayAllVehicles("1. Single Column Update (" + rows + " rows updated):");

    // 2. Multiple Column Update
    // Example: Set all cars of branch 101 as unsold and set price to zero.

    example = new VehicleVO();
    example.setType("CAR"); // set type = 'CAR'
    example.setBranchId(101); // search branch_id = 101
    updateValues = new VehicleVO();
    updateValues.setSold(false); // set sold to false
    updateValues.setListPrice(0); // set price to zero
    rows = VehicleDAO.updateByExample(example, updateValues);
    Utilities.displayAllVehicles("2. Multiple Column Update (" + rows + " rows updated):");

    // 3. Searching & Update Using Null Values
    // Example: To all unsold vehicles with no branch set mileage to zero and
    // price to null.

    example = new VehicleVO();
    example.setSold(false); // search sold = false
    example.setBranchId(null); // search branch_id is null
    updateValues = new VehicleVO();
    updateValues.setMileage(0); // set mileage to zero
    updateValues.setListPrice(null); // set price to null
    rows = VehicleDAO.updateByExample(example, updateValues);
    Utilities.displayAllVehicles("3. Searching & Update Using Null Values (" + rows + " rows updated):");

    // 4. Update with no condition (all rows)
    // Set Purchase Date to 2017-01-01 to all vehicles

    example = new VehicleVO();
    updateValues = new VehicleVO();
    updateValues.setPurchasedOn(Date.valueOf("2017-01-01"));
    rows = VehicleDAO.updateByExample(example, updateValues);
    Utilities.displayAllVehicles("4. Update with no condition, i.e all rows (" + rows + " rows updated):");

    System.out.println(" ");
    System.out.println("=== Example 04 Complete ===");

  }

}
