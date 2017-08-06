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

import daos.CarVO;
import daos.ClientVO;
import daos.VehicleVO;
import daos.primitives.CarDAO;
import daos.primitives.ClientDAO;
import daos.primitives.VehicleDAO;

public class Utilities {

  // Table VEHICLE

  public static void displayAllVehicles(final String title) throws SQLException {
    displayVehicles(title, VehicleDAO.selectByExample(new VehicleVO()));
  }

  public static void displayVehicles(final String title, final List<VehicleVO> vehicles) throws SQLException {
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

    for (VehicleVO c : vehicles) {
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
      t.addCell("" + c.getSold());
    }

    System.out.println(t.render());
  }

  // View CAR

  public static void displayAllCars(final String title) throws SQLException {
    displayCars(title, CarDAO.selectByExample(new CarVO()));
  }

  public static void displayCars(final String title, final List<CarVO> cars) throws SQLException {
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

    for (CarVO c : cars) {
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
      t.addCell("" + c.getSold());
    }

    System.out.println(t.render());
  }

  // Table CLIENT

  public static void displayAllClients(final String title) throws SQLException {
    displayClients(title, ClientDAO.selectByExample(new ClientVO()));
  }

  public static void displayClients(final String title, final List<ClientVO> clients) throws SQLException {
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

    for (ClientVO c : clients) {
      t.addCell("" + c.getId());
      t.addCell(c.getName());
      t.addCell(c.getState());
      t.addCell(c.getDriversLicense());
      t.addCell("" + c.getReferredById());
      t.addCell(mf.format(c.getTotalPurchased()), RIGHT);
      t.addCell("" + c.getVip());
      t.addCell(tsf.format(c.getCreatedAt()));
      t.addCell("" + c.getRowVersion(), RIGHT);
    }

    System.out.println(t.render());
  }

}
