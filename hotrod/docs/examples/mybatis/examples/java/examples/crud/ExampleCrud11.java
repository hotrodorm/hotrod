package examples.crud;

import java.sql.SQLException;

import daos.VehicleVO;
import daos.primitives.VehicleDAO;
import examples.Utilities;

/**
 * Example CRUD 11 - Delete By Example
 * 
 * @author Vladimir Alarcon
 * 
 */
public class ExampleCrud11 {

  public static void main(String[] args) throws SQLException {

    VehicleVO example;
    int rows;

    // 1. Single Column Criteria Delete
    // Example: Delete all MOTORCYCLES

    example = new VehicleVO();
    example.setType("TRUCK");
    rows = VehicleDAO.deleteByExample(example);
    Utilities.displayAllVehicles("1. Single Column Criteria Delete (" + rows + " rows deleted):");

    // 2. Multiple Column Criteria Delete
    // Example: Delete all sold cars

    example = new VehicleVO();
    example.setType("CAR");
    example.setSold(false);
    example.setBranchId(101);
    rows = VehicleDAO.deleteByExample(example);
    Utilities.displayAllVehicles("2. Multiple Column Criteria Delete (" + rows + " rows deleted):");

    // 3. Delete Using Null Values on Criteria
    // Example: Delete all unsold cars without a branch_id

    example = new VehicleVO();
    example.setSold(false);
    example.setType("CAR");
    example.setBranchId(null);
    rows = VehicleDAO.deleteByExample(example);
    Utilities.displayAllVehicles("3. Delete Using Null Values on Criteria (" + rows + " rows deleted):");

  }

}
