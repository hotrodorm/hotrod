package examples.crud;

import java.sql.SQLException;

import daos.VehicleVO;
import daos.primitives.VehicleDAO;

/**
 * Example CRUD 02 - Update by Primary Key
 * 
 * @author Vladimir Alarcon
 * 
 */
public class ExampleCrud02 {

  public static void main(String[] args) throws SQLException {

    VehicleVO delorean = VehicleDAO.select(3);

    // Update by PK: set the mileage of the DeLorean to 270500

    if (delorean != null) {
      delorean.setMileage(270500);
      int rows = VehicleDAO.update(delorean);
      System.out.println("DeLorean mileage updated. Rows updated=" + rows);
    } else {
      System.out.println("Could not update DeLorean. Car not found.");
    }

  }

}
