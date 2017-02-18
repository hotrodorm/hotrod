package tests;

import java.io.IOException;
import java.sql.SQLException;

import org.hotrod.runtime.exceptions.StaleDataException;

import hotrod.test.generation.VehicleDAO;

public class VersionControlTests {

  public static void main(final String[] args) throws IOException, SQLException, StaleDataException {
    testTypes();
  }

  private static void testTypes() throws SQLException, StaleDataException {

    // insert();
    // updateMultipleTimes();
    updateWithCyclingVersionNumber();
    // delete();

  }

  private static void insert() throws SQLException {
    VehicleDAO v = new VehicleDAO();
    v.setName("Fiat - " + System.currentTimeMillis());
    v.setMileage(500);
    v.setVersionNumber((short) 1000); // ignored
    v.insert();

    System.out.println("Inserted with id=" + v.getId() + ", version_number=" + v.getVersionNumber());
  }

  private static void updateMultipleTimes() throws SQLException, StaleDataException {
    VehicleDAO v;
    v = VehicleDAO.select(1);

    for (int i = 0; i < 10; i++) {
      v.setMileage(v.getMileage() + 1);
      v.update();
    }

    System.out.println("updat 10 times.");

  }

  private static void updateWithCyclingVersionNumber() throws SQLException, StaleDataException {
    VehicleDAO v = VehicleDAO.select(3);

    System.out.println("updating with cycling.");

    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    for (int i = 0; i < 2; i++) {
      v.setMileage(v.getMileage() + 1);
      v.update();
    }

    System.out.println("Updated.");
  }

  private static void delete() throws SQLException, StaleDataException {
    VehicleDAO v = VehicleDAO.select(1);

    System.out.println("Will delete in 5s...");

    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    v.delete();

    System.out.println("Deleted.");
  }

}
