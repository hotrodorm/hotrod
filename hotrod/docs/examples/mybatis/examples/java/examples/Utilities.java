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

import daos.BranchVO;
import daos.CarVO;
import daos.ClientVO;
import daos.DailyReportVO;
import daos.VehicleVO;
import daos.VisitVO;
import daos.primitives.BranchDAO;
import daos.primitives.CarDAO;
import daos.primitives.ClientDAO;
import daos.primitives.DailyReportDAO;
import daos.primitives.VehicleDAO;
import daos.primitives.VisitDAO;

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

    Table t = new Table(9, BorderStyle.DESIGN_FORMAL, ShownBorders.HEADER_AND_COLUMNS);
    t.addCell("ID");
    t.addCell("Brand");
    t.addCell("Model");
    t.addCell("Type");
    t.addCell("Mileage");
    t.addCell("Purchased");
    t.addCell("Branch Id", CENTER);
    t.addCell("List Price", RIGHT);
    t.addCell("Sold?");

    for (VehicleVO v : vehicles) {
      t.addCell("" + v.getId());
      t.addCell(v.getBrand());
      t.addCell(v.getModel());
      t.addCell(v.getType());
      t.addCell(df.format(v.getMileage()), RIGHT);
      t.addCell("" + v.getPurchasedOn());
      t.addCell("" + v.getBranchId(), CENTER);
      t.addCell("" + (v.getListPrice() == null ? "null" : mf.format(v.getListPrice())), RIGHT);
      t.addCell("" + v.getSold());
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
    DecimalFormat df = new DecimalFormat("#,##0");
    DecimalFormat mf = new DecimalFormat("'$'#,##0");

    Table t = new Table(8, BorderStyle.DESIGN_FORMAL, ShownBorders.HEADER_AND_COLUMNS);
    t.addCell("ID");
    t.addCell("Brand");
    t.addCell("Model");
    t.addCell("Mileage");
    t.addCell("Purchased");
    t.addCell("Branch Id", RIGHT);
    t.addCell("List Price", RIGHT);
    t.addCell("Sold?");

    for (CarVO c : cars) {
      t.addCell("" + c.getId());
      t.addCell(c.getBrand());
      t.addCell(c.getModel());
      t.addCell(df.format(c.getMileage()), RIGHT);
      t.addCell("" + c.getPurchasedOn());
      t.addCell("" + c.getBranchId(), RIGHT);
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

  // Table DAILY_REPORT

  public static void displayAllDailyReports(final String title) throws SQLException {
    displayDailyReports(title, DailyReportDAO.selectByExample(new DailyReportVO()));
  }

  public static void displayDailyReports(final String title, final List<DailyReportVO> reports) throws SQLException {
    System.out.println(" ");
    System.out.println(title);

    CellStyle RIGHT = new CellStyle(HorizontalAlign.right);
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    DecimalFormat mf = new DecimalFormat("'$'#,##0");

    Table t = new Table(3, BorderStyle.DESIGN_FORMAL, ShownBorders.HEADER_AND_COLUMNS);
    t.addCell("Report Date");
    t.addCell("Branch ID", RIGHT);
    t.addCell("Total Sold", RIGHT);

    for (DailyReportVO r : reports) {
      t.addCell(df.format(r.getReportDate()));
      t.addCell("" + r.getBranchId(), RIGHT);
      t.addCell(mf.format(r.getTotalSold()), RIGHT);
    }

    System.out.println(t.render());
  }

  // Table VISITS

  public static void displayAllVisits(final String title) throws SQLException {
    displayVisits(title, VisitDAO.selectByExample(new VisitVO()));
  }

  public static void displayVisits(final String title, final List<VisitVO> visits) throws SQLException {
    System.out.println(" ");
    System.out.println(title);

    CellStyle RIGHT = new CellStyle(HorizontalAlign.right);
    SimpleDateFormat tsf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    Table t = new Table(3, BorderStyle.DESIGN_FORMAL, ShownBorders.HEADER_AND_COLUMNS);
    t.addCell("Recorded At");
    t.addCell("Branch ID", RIGHT);
    t.addCell("Notes");

    for (VisitVO v : visits) {
      t.addCell(tsf.format(v.getRecordedAt()));
      t.addCell("" + v.getBranchId(), RIGHT);
      t.addCell(v.getNotes());
    }

    System.out.println(t.render());
  }

  // Table BRANCH

  public static void displayAllBranches(final String title) throws SQLException {
    displayBranches(title, BranchDAO.selectByExample(new BranchVO()));
  }

  public static void displayBranches(final String title, final List<BranchVO> branches) throws SQLException {
    System.out.println(" ");
    System.out.println(title);

    CellStyle RIGHT = new CellStyle(HorizontalAlign.right);
    DecimalFormat mf = new DecimalFormat("'$'#,##0");

    Table t = new Table(3, BorderStyle.DESIGN_FORMAL, ShownBorders.HEADER_AND_COLUMNS);
    t.addCell("ID");
    t.addCell("Name");
    t.addCell("Current Cash", RIGHT);

    for (BranchVO b : branches) {
      t.addCell("" + b.getId());
      t.addCell(b.getName());
      t.addCell(mf.format(b.getCurrentCash()), RIGHT);
    }

    System.out.println(t.render());
  }

}
