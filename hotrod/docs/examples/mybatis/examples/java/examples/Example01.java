package examples;

import java.sql.Date;
import java.sql.SQLException;

import daos.VehicleDAO;

/**
 * CRUD operations on tables
 * 
 * @author valarcon
 * 
 */
public class Example01 {

  public static void main(String[] args) throws SQLException {

    // List all vehicles

    System.out.println();
    System.out.println("List of all vehicles, before changes:");
    System.out.println("ID, BRAND, MODEL, USED, CURRENT_MILEAGE, PURCHASED_ON");
    VehicleDAO example1 = new VehicleDAO();
    for (VehicleDAO c : VehicleDAO.selectByExample(example1)) {
      System.out.println(c);
    }

    // Insert a new vehicle

    System.out.println(" ");

    VehicleDAO skoda = new VehicleDAO();
    skoda.setBrand("Skoda");
    skoda.setModel("Octavia");
    skoda.setUsed(false);
    skoda.setCurrentMileage(7);
    skoda.setPurchasedOn(new Date(System.currentTimeMillis()));
    int rows = skoda.insert();
    System.out.println("New vehicle Skoda added. New ID=" + skoda.getId() + ". Rows inserted=" + rows);

    // Select by PK: retrieve the DeLorean row (id=3)

    System.out.println(" ");

    VehicleDAO delorean = VehicleDAO.select(3);

    System.out.println("DeLorean " + (delorean == null ? "not " : "") + "found.");

    // Update by PK: set the mileage of the DeLorean to 270500

    System.out.println(" ");

    if (delorean != null) {
      delorean.setCurrentMileage(270500);
      rows = delorean.update();
      System.out.println("DeLorean updated. Rows updated=" + rows);
    } else {
      System.out.println("Could not update DeLorean. Car not found.");
    }

    // Delete by PK: delete the Toyota (id=2)

    System.out.println(" ");

    VehicleDAO toyota = new VehicleDAO();
    toyota.setId(2);
    rows = toyota.delete();
    if (rows > 0) {
      System.out.println("Toyota deleted.");
    } else {
      System.out.println("Could not delete Toyota. Car not found.");
    }

    // List all vehicles again

    System.out.println(" ");

    System.out.println();
    System.out.println("List of all vehicles, after all changes:");
    System.out.println("ID, BRAND, MODEL, USED, CURRENT_MILEAGE, PURCHASED_ON");
    for (VehicleDAO c : VehicleDAO.selectByExample(example1)) {
      System.out.println(c);
    }

  }

}
