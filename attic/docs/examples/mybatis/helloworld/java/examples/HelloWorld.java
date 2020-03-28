package examples;

import java.sql.Date;
import java.sql.SQLException;

import daos.VehicleDAO;

public class HelloWorld {

  public static void main(String[] args) throws SQLException {

    System.out.println("Example 1 - Hello World - Starting");

    VehicleDAO skoda = new VehicleDAO();
    skoda.setBrand("Skoda");
    skoda.setModel("Octavia");
    skoda.setUsed(false);
    skoda.setCurrentMileage(7);
    skoda.setPurchasedOn(new Date(System.currentTimeMillis()));
    skoda.insert();

    System.out.println("New vehicle Skoda added, with id " + skoda.getId() + ".");

    System.out.println("Example 1 - Hello World - Finished");

  }

}
