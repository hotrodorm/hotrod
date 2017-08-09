package examples.crud;

import java.sql.SQLException;

import daos.VehicleVO;
import daos.primitives.VehicleDAO;

/**
 * Example CRUD 01 - Select by Primary Key
 * 
 * @author Vladimir Alarcon
 * 
 */
public class ExampleCrud01 {

  public static void main(String[] args) throws SQLException {
    VehicleVO car = VehicleDAO.select(3);
    System.out.println("car=" + car);
  }

}
