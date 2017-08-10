package examples.crud1;

import java.sql.SQLException;

import daos.VehicleVO;
import daos.primitives.VehicleDAO;

/**
 * Example CRUD 101 - Select by Primary Key
 * 
 * @author Vladimir Alarcon
 * 
 */
public class ExampleCrud101 {

  public static void main(String[] args) throws SQLException {
    VehicleVO car = VehicleDAO.select(3);
    System.out.println("car=" + car);
  }

}
