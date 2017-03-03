package examples;

import java.sql.Date;
import java.sql.SQLException;
import java.text.DecimalFormat;

import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.CellStyle.HorizontalAlign;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import daos.VehicleDAO;

/**
 * CRUD Operations on Tables
 * 
 * @author valarcon
 * 
 */
public class Example01 {

  public static void main(String[] args) throws SQLException {

    System.out.println("=== Running Example 01 - CRUD Operations on Tables ===");

    // List all vehicles

    listAllVehicles();
    System.out.println(" ");

    // 1. Insert a vehicle

    VehicleDAO skoda = new VehicleDAO();
    // skoda.setId(...); // ignored: the DB will generate the PK
    skoda.setBrand("Skoda");
    skoda.setModel("Octavia");
    skoda.setType("CAR");
    skoda.setVin("VN4408");
    skoda.setEngineNumber("EN9401");
    skoda.setMileage(12100);
    skoda.setPurchasedOn(Date.valueOf("2017-02-17"));
    skoda.setBranchId(102);
    skoda.setListPrice(22400);
    skoda.setSold(false);
    int rows = skoda.insert();
    System.out.println("1. New vehicle Skoda added. New ID=" + skoda.getId() + ". Rows inserted=" + rows);

    // 2. Select by PK: retrieve the DeLorean row (id=3)

    VehicleDAO delorean = VehicleDAO.select(3);
    System.out.println("2. DeLorean " + (delorean == null ? "not " : "") + "found.");

    // 3. Update by PK: set the mileage of the DeLorean to 270500

    if (delorean != null) {
      delorean.setMileage(270500);
      rows = delorean.update();
      System.out.println("3. DeLorean mileage updated. Rows updated=" + rows);
    } else {
      System.out.println("3. Could not update DeLorean. Car not found.");
    }

    // 4. Delete by PK: delete the Peterbilt 379 (id=2)

    VehicleDAO truck = new VehicleDAO();
    truck.setId(2); // set the PK value
    rows = truck.delete(); // delete by PK
    if (rows > 0) {
      System.out.println("4. Peterbilt 379 deleted.");
    } else {
      System.out.println("4. Could not delete Peterbilt 379. Vehicle not found.");
    }

    // List all vehicles again

    listAllVehicles();
    System.out.println("=== Example 01 Complete ===");

  }

  // Helper methods

  private static void listAllVehicles() throws SQLException {
    System.out.println(" ");
    System.out.println("Listing of all vehicles:");

    CellStyle RIGHT = new CellStyle(HorizontalAlign.right);
    CellStyle CENTER = new CellStyle(HorizontalAlign.center);
    DecimalFormat df = new DecimalFormat("#,##0");
    DecimalFormat mf = new DecimalFormat("'$'#,##0");

    Table t = new Table(11, BorderStyle.DESIGN_FORMAL, ShownBorders.HEADER_AND_COLUMNS);
    t.addCell("ID");
    t.addCell("Brand");
    t.addCell("Model");
    t.addCell("Type");
    t.addCell("VIN");
    t.addCell("Engine#");
    t.addCell("Mileage");
    t.addCell("Purchased");
    t.addCell("Branch Id", CENTER);
    t.addCell("List Price", RIGHT);
    t.addCell("Sold?");

    VehicleDAO example1 = new VehicleDAO();
    for (VehicleDAO c : VehicleDAO.selectByExample(example1)) {
      t.addCell("" + c.getId());
      t.addCell(c.getBrand());
      t.addCell(c.getModel());
      t.addCell(c.getType());
      t.addCell(c.getVin());
      t.addCell(c.getEngineNumber());
      t.addCell(df.format(c.getMileage()), RIGHT);
      t.addCell("" + c.getPurchasedOn());
      t.addCell("" + c.getBranchId(), CENTER);
      t.addCell("" + (c.getListPrice() == null ? "null" : mf.format(c.getListPrice())), RIGHT);
      t.addCell("" + c.isSold());
    }

    System.out.println(t.render());
  }

}
