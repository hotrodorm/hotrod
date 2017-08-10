package examples.crud2;

import java.sql.Date;
import java.sql.SQLException;

import daos.CarVO;
import daos.primitives.CarDAO;
import examples.Utilities;

/**
 * Example CRUD 209 - Update on Views
 * 
 * @author Vladimir Alarcon
 * 
 */
public class ExampleCrud209 {

  public static void main(String[] args) throws SQLException {

    CarVO example;
    CarVO updateValues;
    int rows;

    // 1. Single Column Update
    // Example: Set mileage to 10 to all cars on branch 101.

    example = new CarVO();
    example.setBranchId(101); // search branch_id = 101
    updateValues = new CarVO();
    updateValues.setMileage(10); // set mileage to 10
    rows = CarDAO.updateByExample(example, updateValues);
    Utilities.displayAllCars("1. Single Column Update (" + rows + " rows updated):");

    // 2. Multiple Column Update
    // Example: Set all cars of model "Soul" on branch 101, as unsold and set
    // price to zero.

    example = new CarVO();
    example.setBrand("Soul"); // search by model = "Soul"
    example.setBranchId(101); // search branch_id = 101
    updateValues = new CarVO();
    updateValues.setSold(false); // set sold to false
    updateValues.setListPrice(0); // set price to zero
    rows = CarDAO.updateByExample(example, updateValues);
    Utilities.displayAllCars("2. Multiple Column Update (" + rows + " rows updated):");

    // 3. Searching & Update Using Null Values
    // Example: To all unsold vehicles with no branch set mileage to zero and
    // price to null.

    example = new CarVO();
    example.setSold(false); // search sold = false
    example.setBranchId(null); // search branch_id is null
    updateValues = new CarVO();
    updateValues.setMileage(0); // set mileage to zero
    updateValues.setListPrice(null); // set price to null
    rows = CarDAO.updateByExample(example, updateValues);
    Utilities.displayAllCars("3. Searching & Update Using Null Values (" + rows + " rows updated):");

    // 4. Update with no filter condition (all rows)
    // Set Purchase Date to 2017-01-01 to all vehicles

    example = new CarVO();
    updateValues = new CarVO();
    updateValues.setPurchasedOn(Date.valueOf("2017-01-01"));
    rows = CarDAO.updateByExample(example, updateValues);
    Utilities.displayAllCars("4. Update with no condition, i.e all rows (" + rows + " rows updated):");

  }

}
