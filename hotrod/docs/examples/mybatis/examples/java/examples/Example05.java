package examples;

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
 * Delete by Example
 * 
 * @author valarcon
 * 
 */
public class Example05 {

  public static void main(String[] args) throws SQLException {

    System.out.println("=== Running Example 05 - Delete by Example ===");
    VehicleDAO example;
    int rows;

    // 1. Single Column Criteria Delete
    // Example: Delete all MOTORCYCLES

    example = new VehicleDAO();
    example.setType("MOTORCYCLE");
    rows = VehicleDAO.deleteByExample(example);
    displayAllVehicles("1. Single Column Criteria Delete (" + rows + " rows deleted):");

    // 2. Multiple Column Criteria Delete
    // Example: Delete all sold cars

    example = new VehicleDAO();
    example.setType("CAR");
    example.setSold(true);
    rows = VehicleDAO.deleteByExample(example);
    displayAllVehicles("2. Multiple Column Criteria Delete (" + rows + " rows deleted):");

    // 3. Delete Using Null Values on Criteria
    // Example: Delete all unsold cars without a branch_id

    example = new VehicleDAO();
    example.setSold(false);
    example.setType("CAR");
    example.setBranchId(null);
    rows = VehicleDAO.deleteByExample(example);
    displayAllVehicles("3. Delete Using Null Values on Criteria (" + rows + " rows deleted):");

    System.out.println(" ");
    System.out.println("=== Example 05 Complete ===");

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
