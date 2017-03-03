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
import daos.primitives.VehicleDAOPrimitives.VehicleDAOOrderBy;

/**
 * Select by Example
 * 
 * @author valarcon
 * 
 */
public class Example03 {

  public static void main(String[] args) throws SQLException {

    System.out.println("=== Running Example 03 - Select by Example ===");
    VehicleDAO example;

    // 1. select by a single column - (type 'CAR')

    example = new VehicleDAO();
    example.setType("CAR");
    List<VehicleDAO> cars = VehicleDAO.selectByExample(example);
    displayVehicles("1. Select All Vehicles of type 'CAR':", cars);

    // 2. select by multiple columns (BRAND & MODEL)

    example = new VehicleDAO();
    example.setBrand("Toyota");
    example.setModel("Tercel");
    List<VehicleDAO> tercel = VehicleDAO.selectByExample(example);
    displayVehicles("2. Select All Vehicles 'Toyota Tercel':", tercel);

    // 3. select using null values (BRAND + MODEL + no BRANCH_ID)

    example = new VehicleDAO();
    example.setBrand("Toyota");
    example.setModel("Tercel");
    example.setBranchId(null); // This forces to search for null branch_id
    List<VehicleDAO> tercelNoBranch = VehicleDAO.selectByExample(example);
    displayVehicles("3. Select All Vehicles 'Toyota Tercel' with no branch:", tercelNoBranch);

    // 4. Select unsold vehicles, with order
    // Sort ascending by type, then descending by brand, then ascending by model

    example = new VehicleDAO();
    example.setSold(false);
    List<VehicleDAO> unsold = VehicleDAO.selectByExample(example, VehicleDAOOrderBy.TYPE, VehicleDAOOrderBy.BRAND$DESC,
        VehicleDAOOrderBy.MODEL);
    displayVehicles("4. Select unsold vehicles, with order:", unsold);

    // 5. Select unsold vehicles, with case insensitive order
    // Sort case-insensitive by brand, then case-insensitive by type descending

    example = new VehicleDAO();
    example.setSold(false);
    List<VehicleDAO> unsold2 = VehicleDAO.selectByExample(example, VehicleDAOOrderBy.BRAND$CASEINSENSITIVE,
        VehicleDAOOrderBy.TYPE$DESC_CASEINSENSITIVE);
    displayVehicles("5. Select unsold vehicles, with case insensitive order:", unsold2);

    // 6. Select unsold vehicles, with case insensitive stable-forward order
    // Sort case-insensitive and stable-forward by brand

    example = new VehicleDAO();
    example.setSold(false);
    List<VehicleDAO> unsold3 = VehicleDAO.selectByExample(example,
        VehicleDAOOrderBy.BRAND$CASEINSENSITIVE_STABLE_FORWARD);
    displayVehicles("6. Select unsold vehicles, with case insensitive stable-forward order:", unsold3);

    System.out.println(" ");
    System.out.println("=== Example 03 Complete ===");

  }

  // Helper methods

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
