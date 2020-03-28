package examples.crud1;

import java.sql.Date;
import java.sql.SQLException;

import daos.VehicleVO;
import daos.primitives.VehicleDAO;

/**
 * Example CRUD 104 - Insert with Identity PK
 * 
 * @author Vladimir Alarcon
 * 
 */
public class ExampleCrud104 {

  public static void main(String[] args) throws SQLException {

    VehicleVO v = new VehicleVO();
    // v.setId(123); // ignored: the DB will generate the PK
    v.setBrand("Skoda");
    v.setModel("Octavia");
    v.setType("CAR");
    v.setMileage(12100);
    v.setPurchasedOn(Date.valueOf("2017-02-17"));
    v.setBranchId(102);
    v.setListPrice(22400);
    v.setSold(false);
    int rows = VehicleDAO.insert(v);
    System.out.println("Vehicle inserted (identity PK). ID=" + v.getId() + ". Rows inserted=" + rows);

  }

}
