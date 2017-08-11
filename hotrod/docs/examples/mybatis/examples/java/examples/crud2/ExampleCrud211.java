package examples.crud2;

import java.sql.Date;
import java.sql.SQLException;

import daos.CarVO;
import daos.primitives.CarDAO;
import examples.Utilities;

/**
 * Example CRUD 211 - Insert on Views
 * 
 * @author Vladimir Alarcon
 * 
 */
public class ExampleCrud211 {

  public static void main(String[] args) throws SQLException {

    // 1. Insert By Example on a View

    CarVO v = new CarVO();
    v.setId(123);
    v.setBrand("Skoda");
    v.setModel("Octavia");
    v.setType("CAR");
    v.setVin("VN4408");
    v.setEngineNumber("EN9401");
    v.setMileage(12100);
    v.setPurchasedOn(Date.valueOf("2016-10-11"));
    v.setBranchId(102);
    v.setListPrice(22400);
    v.setSold(false);
    int rows = CarDAO.insertByExample(v);
    Utilities.displayAllCars("1. Car inserted by example. rows=" + rows);

  }

}
