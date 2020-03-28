package examples.crud2;

import java.sql.SQLException;

import daos.VehicleVO;
import daos.primitives.VehicleDAO;
import examples.Utilities;

/**
 * Example CRUD 203 - Delete By Example
 * 
 * @author Vladimir Alarcon
 * 
 */
public class ExampleCrud203 {

  public static void main(String[] args) throws SQLException {

    VehicleVO example;
    int rows;

    // 1. Single Column Criteria Delete
    // Example: Delete all trucks

    example = new VehicleVO();
    example.setType("TRUCK");
    rows = VehicleDAO.deleteByExample(example);
    Utilities.displayAllVehicles("1. Single Column Criteria Delete (" + rows + " rows deleted):");

    // 2. Multiple Column Criteria Delete
    // Example: Delete all unsold cars on branch 101

    example = new VehicleVO();
    example.setType("CAR");
    example.setSold(false);
    example.setBranchId(101);
    rows = VehicleDAO.deleteByExample(example);
    Utilities.displayAllVehicles("2. Multiple Column Criteria Delete (" + rows + " rows deleted):");

    // 3. Delete Using Null Values on Criteria
    // Example: Delete all unsold cars without a branch

    example = new VehicleVO();
    example.setSold(false);
    example.setType("CAR");
    example.setBranchId(null);
    rows = VehicleDAO.deleteByExample(example);
    Utilities.displayAllVehicles("3. Delete Using Null Values on Criteria (" + rows + " rows deleted):");

  }

}
