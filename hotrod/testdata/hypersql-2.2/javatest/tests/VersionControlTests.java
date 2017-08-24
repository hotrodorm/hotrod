package tests;

import java.io.IOException;
import java.sql.SQLException;

import org.hotrod.runtime.exceptions.StaleDataException;

import hotrod.test.generation.VehicleVO;
import hotrod.test.generation.primitives.VehicleDAO;

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
    VehicleVO v = new VehicleVO();
    v.setName("Fiat - " + System.currentTimeMillis());
    v.setMileage(500);
    v.setVersionNumber((short) 1000); // ignored
    VehicleDAO.insert(v);

    System.out.println("Inserted with id=" + v.getId() + ", version_number=" + v.getVersionNumber());
  }

  private static void updateMultipleTimes() throws SQLException, StaleDataException {
    VehicleVO v;
    v = VehicleDAO.select(1);

    for (int i = 0; i < 10; i++) {
      v.setMileage(v.getMileage() + 1);
      VehicleDAO.update(v);
    }

    System.out.println("updat 10 times.");

  }

  private static void updateWithCyclingVersionNumber() throws SQLException, StaleDataException {
    VehicleVO v = VehicleDAO.select(1);

    System.out.println("updating with cycling.");

    for (int i = 0; i < 2; i++) {
      v.setMileage(v.getMileage() + 1);
      VehicleDAO.update(v);
    }

    System.out.println("Updated.");
  }

  private static void delete() throws SQLException, StaleDataException {
    VehicleVO v = VehicleDAO.select(1);

    System.out.println("Will delete in 5s...");

    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    VehicleDAO.delete(v);

    System.out.println("Deleted.");
  }

}
