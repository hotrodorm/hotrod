package examples;

import java.sql.Date;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;

import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.CellStyle.HorizontalAlign;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import daos.VehicleDAO;

/**
 * Update by Example
 * 
 * @author valarcon
 * 
 */
public class Example04 {

  public static void main(String[] args) throws SQLException {

    System.out.println("=== Running Example 04 - Update by Example ===");
    VehicleDAO example;
    VehicleDAO updateValues;
    int rows;
    displayAllVehicles("Initial Data on Table VEHICLE:");

    // 1. Single Column Update
    // Example: Set mileage to 10 to all cars on branch 101.

    example = new VehicleDAO();
    example.setBranchId(103); // search branch_id = 103
    updateValues = new VehicleDAO();
    updateValues.setMileage(10); // set mileage to 10
    rows = VehicleDAO.updateByExample(example, updateValues);
    displayAllVehicles("1. Single Column Update (" + rows + " rows updated):");

    // 2. Multiple Column Update
    // Example: Set all cars of branch 101 as unsold and set price to zero.

    example = new VehicleDAO();
    example.setType("CAR"); // set type = 'CAR'
    example.setBranchId(101); // search branch_id = 101
    updateValues = new VehicleDAO();
    updateValues.setSold(false); // set sold to false
    updateValues.setListPrice(0); // set price to zero
    rows = VehicleDAO.updateByExample(example, updateValues);
    displayAllVehicles("2. Multiple Column Update (" + rows + " rows updated):");

    // 3. Searching & Update Using Null Values
    // Example: To all unsold vehicles with no branch set mileage to zero and
    // price to null.

    example = new VehicleDAO();
    example.setSold(false); // search sold = false
    example.setBranchId(null); // search branch_id is null
    updateValues = new VehicleDAO();
    updateValues.setMileage(0); // set mileage to zero
    updateValues.setListPrice(null); // set price to null
    rows = VehicleDAO.updateByExample(example, updateValues);
    displayAllVehicles("3. Searching & Update Using Null Values (" + rows + " rows updated):");

    // 4. Update with no condition (all rows)
    // Set Purchase Date to 2017-01-01 to all vehicles

    example = new VehicleDAO();
    updateValues = new VehicleDAO();
    updateValues.setPurchasedOn(Date.valueOf("2017-01-01"));
    rows = VehicleDAO.updateByExample(example, updateValues);
    displayAllVehicles("4. Update with no condition, i.e all rows (" + rows + " rows updated):");

    System.out.println(" ");
    System.out.println("=== Example 04 Complete ===");

  }

  // Helper methods

  private static void displayAllVehicles(final String title) throws SQLException {
    displayVehicles(title, VehicleDAO.selectByExample(new VehicleDAO()));
  }

  private static void displayVehicles(final String title, final List<VehicleDAO> vehicles) throws SQLException {
    System.out.println(" ");
    System.out.println(title);

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

    for (VehicleDAO c : vehicles) {
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
