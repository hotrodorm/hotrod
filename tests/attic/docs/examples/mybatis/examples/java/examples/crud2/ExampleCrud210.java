package examples.crud2;

import java.sql.SQLException;

import daos.CarVO;
import daos.primitives.CarDAO;
import examples.Utilities;

/**
 * Example CRUD 210 - Delete on Views
 * 
 * @author Vladimir Alarcon
 * 
 */
public class ExampleCrud210 {

  public static void main(String[] args) throws SQLException {

    CarVO example;
    int rows;

    // 1. Single Column Criteria Delete
    // Example: Delete all cars of model "Yaris"

    example = new CarVO();
    example.setModel("Yaris");
    rows = CarDAO.deleteByExample(example);
    Utilities.displayAllCars("1. Single Column Criteria Delete (" + rows + " rows deleted):");

    // 2. Multiple Column Criteria Delete
    // Example: Delete all unsold cars on branch 101

    example = new CarVO();
    example.setSold(false);
    example.setBranchId(101);
    rows = CarDAO.deleteByExample(example);
    Utilities.displayAllCars("2. Multiple Column Criteria Delete (" + rows + " rows deleted):");

    // 3. Delete Using Null Values on Criteria
    // Example: Delete all unsold cars without a branch

    example = new CarVO();
    example.setSold(false);
    example.setBranchId(null);
    rows = CarDAO.deleteByExample(example);
    Utilities.displayAllCars("3. Delete Using Null Values on Criteria (" + rows + " rows deleted):");

  }

}
