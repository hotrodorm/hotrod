package examples;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.CellStyle.HorizontalAlign;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import daos.BranchDAO;
import daos.ClientDAO;
import daos.VehicleDAO;
import daos.primitives.ClientDAOPrimitives.ClientDAOOrderBy;
import daos.primitives.VehicleDAOPrimitives.VehicleDAOOrderBy;

/**
 * Navigating Foreign Keys
 * 
 * @author valarcon
 * 
 */
public class Example06 {

  public static void main(String[] args) throws SQLException {

    System.out.println("=== Running Example 06 - Navigating Foreign Keys ===");

    // 1. Navigate to parent
    // Example: Get the parent branch row for a vehicle row, using the branch_id
    // FK that points to the branch table

    VehicleDAO v = VehicleDAO.select(6); // get the Yamaha motorcycle
    BranchDAO b = v.selectParentBranchDAO().byBranchId();
    System.out.println("1. Navigate to parent - the branch for the Yamaha motorcycle is: " + b.getName());

    // 2. Navigate to Children
    // Example: Get all the vehicles for a branch 101

    BranchDAO b2 = BranchDAO.select(101);
    List<VehicleDAO> vehicles2 = b2.selectChildrenVehicleDAO().byBranchId();
    displayVehicles("2. Navigate to Children:", vehicles2);

    // 3. Navigate to Children With Ordering
    // Example: Get all the vehicles for a branch row, order by brand, then
    // purchase_on descending

    BranchDAO b3 = BranchDAO.select(101);
    List<VehicleDAO> vehicles3 = b3.selectChildrenVehicleDAO().byBranchId(VehicleDAOOrderBy.BRAND,
        VehicleDAOOrderBy.PURCHASED_ON$DESC);
    displayVehicles("3. Navigate to Children With Ordering:", vehicles3);

    // 4. Reflexive Navigate to Parent
    // Example: Find out who referred Gunilla

    ClientDAO c1 = ClientDAO.select(24); // Gunilla's ID
    ClientDAO p1 = c1.selectParentClientDAO().byReferredById();
    System.out.println(" ");
    System.out.println("4. Reflexive Navigate to Parent - Gunilla's referrer is: " + p1.getName());

    // 5. Reflexive Navigate to Children
    // Example: Find out who has been referred by Jane

    ClientDAO c2 = ClientDAO.select(21); // Jane's ID
    List<ClientDAO> clients2 = c2.selectChildrenClientDAO().byReferredById();
    displayClients("5. Reflexive Navigate to Children:", clients2);

    // 6. Reflexive Navigate to Children With Order
    // Example: Find out who has been referred by Jane, ordered by creation date
    // descending, then by name (ascending, case insensitive).

    ClientDAO c3 = ClientDAO.select(21); // Jane's ID
    List<ClientDAO> clients3 = c2.selectChildrenClientDAO().byReferredById(ClientDAOOrderBy.CREATED_AT$DESC,
        ClientDAOOrderBy.NAME$CASEINSENSITIVE);
    displayClients("6. Reflexive Navigate to Children With Order:", clients3);

    System.out.println(" ");
    System.out.println("=== Example 06 Complete ===");

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

  private static void displayClients(final String title, final List<ClientDAO> clients) throws SQLException {
    System.out.println(" ");
    System.out.println(title);

    CellStyle RIGHT = new CellStyle(HorizontalAlign.right);
    CellStyle CENTER = new CellStyle(HorizontalAlign.center);
    DecimalFormat df = new DecimalFormat("#,##0");
    DecimalFormat mf = new DecimalFormat("'$'#,##0");
    SimpleDateFormat tsf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    Table t = new Table(9, BorderStyle.DESIGN_FORMAL, ShownBorders.HEADER_AND_COLUMNS);
    t.addCell("ID");
    t.addCell("Name");
    t.addCell("State");
    t.addCell("Driver's License");
    t.addCell("Referred By");
    t.addCell("Total Purchased");
    t.addCell("VIP?");
    t.addCell("Created At");
    t.addCell("RV#");

    for (ClientDAO c : clients) {
      t.addCell("" + c.getId());
      t.addCell(c.getName());
      t.addCell(c.getState());
      t.addCell(c.getDriversLicense());
      t.addCell("" + c.getReferredById());
      t.addCell(mf.format(c.getTotalPurchased()), RIGHT);
      t.addCell("" + c.isVip());
      t.addCell(tsf.format(c.getCreatedAt()));
      t.addCell("" + c.getRowVersion(), RIGHT);
    }

    System.out.println(t.render());
  }

}
