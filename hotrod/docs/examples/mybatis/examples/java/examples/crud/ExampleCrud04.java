package examples.crud;

import java.sql.Date;
import java.sql.SQLException;

import daos.VehicleVO;
import daos.primitives.VehicleDAO;

/**
 * Example CRUD 04 - Insert with Identity PK
 * 
 * @author Vladimir Alarcon
 * 
 */
public class ExampleCrud04 {

  public static void main(String[] args) throws SQLException {

    VehicleVO v = new VehicleVO();
    // v.setId(123); // ignored: the DB will generate the PK
    v.setBrand("Skoda");
    v.setModel("Octavia");
    v.setType("CAR");
    v.setVin("VN4408");
    v.setEngineNumber("EN9401");
    v.setMileage(12100);
    v.setPurchasedOn(Date.valueOf("2017-02-17"));
    v.setBranchId(102);
    v.setListPrice(22400);
    v.setSold(false);
    int rows = VehicleDAO.insert(v);
    System.out.println("Vehicle inserted (identity PK). ID=" + v.getId() + ". Rows inserted=" + rows);

  }

}
