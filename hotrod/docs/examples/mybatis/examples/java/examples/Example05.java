package examples;

import java.sql.SQLException;

import daos.VehicleDAO;

/**
 * Example 05 - Delete by Example
 * 
 * @author valarcon
 * 
 */
public class Example05 {

  public static void main(String[] args) throws SQLException {

    System.out.println("=== Running Example 05 - Delete by Example ===");
    VehicleDAO example;
    int rows;

    // 1. Single Column Criteria Delete
    // Example: Delete all MOTORCYCLES

    example = new VehicleDAO();
    example.setType("MOTORCYCLE");
    rows = VehicleDAO.deleteByExample(example);
    Utilities.displayAllVehicles("1. Single Column Criteria Delete (" + rows + " rows deleted):");

    // 2. Multiple Column Criteria Delete
    // Example: Delete all sold cars

    example = new VehicleDAO();
    example.setType("CAR");
    example.setSold(true);
    rows = VehicleDAO.deleteByExample(example);
    Utilities.displayAllVehicles("2. Multiple Column Criteria Delete (" + rows + " rows deleted):");

    // 3. Delete Using Null Values on Criteria
    // Example: Delete all unsold cars without a branch_id

    example = new VehicleDAO();
    example.setSold(false);
    example.setType("CAR");
    example.setBranchId(null);
    rows = VehicleDAO.deleteByExample(example);
    Utilities.displayAllVehicles("3. Delete Using Null Values on Criteria (" + rows + " rows deleted):");

    System.out.println(" ");
    System.out.println("=== Example 05 Complete ===");

  }

}
