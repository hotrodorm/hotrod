package examples.crud;

import java.sql.SQLException;

import daos.VehicleVO;
import daos.primitives.VehicleDAO;

/**
 * Example CRUD 03 - Delete by Primary Key
 * 
 * @author Vladimir Alarcon
 * 
 */
public class ExampleCrud03 {

  public static void main(String[] args) throws SQLException {

    // Delete the Peterbilt 379 (id=2)

    VehicleVO truck = new VehicleVO();
    truck.setId(2); // set the PK value
    int rows = VehicleDAO.delete(truck);
    if (rows > 0) {
      System.out.println("Peterbilt 379 deleted.");
    } else {
      System.out.println("Could not delete Peterbilt 379. Vehicle not found.");
    }

  }

}
