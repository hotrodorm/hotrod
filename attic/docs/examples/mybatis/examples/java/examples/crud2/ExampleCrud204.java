package examples.crud2;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;

import daos.BranchVO;
import daos.ClientVO;
import daos.DailyReportVO;
import daos.VehicleVO;
import daos.VisitVO;
import daos.primitives.BranchDAO;
import daos.primitives.ClientDAO;
import daos.primitives.DailyReportDAO;
import daos.primitives.VehicleDAO;
import daos.primitives.VisitDAO;
import examples.Utilities;

/**
 * Example CRUD 204 - Insert By Example
 * 
 * @author Vladimir Alarcon
 * 
 */
public class ExampleCrud204 {

  public static void main(String[] args) throws SQLException {

    int rows;

    // 1. Insert By Example using Identity

    VehicleVO v = new VehicleVO();
    // v.setId(123); // ignored: the DB will generate the PK
    v.setBrand("Skoda");
    v.setModel("Octavia");
    v.setType("CAR");
    v.setMileage(12100);
    // v.setPurchasedOn(...); // unset - will use table default (today)
    v.setBranchId(102);
    v.setListPrice(22400);
    // v.setSold(...); // unset - will use table default (false)
    rows = VehicleDAO.insertByExample(v);
    Utilities.displayAllVehicles("1. Vehicle inserted by example (using identity PK). ID=" + v.getId()
        + ". Rows inserted=" + rows + "\n/// Updated list of vehicles ///");

    // 2. Insert By Example using Sequence

    ClientVO c = new ClientVO();
    // c.setId(123); // ignored: the PK value will be generated by the sequence
    c.setCreatedAt(Timestamp.valueOf("2017-02-22 18:15:21"));
    c.setName("Yul Brynner");
    c.setState("NM");
    c.setDriversLicense("TB1-450V");
    c.setReferredById(null);
    c.setTotalPurchased(0L);
    // c.setVip(...); // unset - will use table default (false)
    c.setRowVersion(0L);
    rows = ClientDAO.insertByExample(c);
    Utilities.displayAllClients("2. Client inserted by example (using sequence). ID=" + c.getId() + ". Rows inserted="
        + rows + "\n/// Updated list of clients ///");

    // 3. Insert By Example with non-autogenerated PK

    DailyReportVO r = new DailyReportVO();
    r.setBranchId(104); // PK column 1
    r.setReportDate(Date.valueOf("2017-02-28")); // PK column 2
    // r.setTotalSold(...); // unset - will use table default (0)
    rows = DailyReportDAO.insertByExample(r);
    Utilities.displayAllDailyReports("3. Daily Report inserted by example (table with PK, but no auto-generation). "
        + "Rows inserted=" + rows + "\n/// Updated list of daily reports ///");

    // 4. Insert By Example with no PK

    VisitVO vi = new VisitVO();
    vi.setRecordedAt(Timestamp.valueOf("2017-03-01 11:30:00"));
    vi.setBranchId(105);
    // vi.setNotes(...); // unset - will use table default ("no notes taken")
    rows = VisitDAO.insertByExample(vi);
    Utilities.displayAllVisits(
        "4. Visit inserted by example (table with no PK). Rows inserted=" + rows + "\n/// Updated list of visits ///");

    // 5. Insert By Example using Optional Identity (not specified)

    BranchVO b = new BranchVO();
    // b.setId(null); // ignored - the DB will generate the PK
    b.setName("Wichita");
    // b.setCurrentCash(...); // unset - will use table default (0)
    rows = BranchDAO.insertByExample(b);
    Utilities.displayAllBranches("5. Branch inserted by example (identity PK not specified). ID=" + b.getId()
        + ". Rows inserted=" + rows + "\n/// Updated list of visits ///");

    // 6. Insert By Example using Optional Identity (specified)

    b = new BranchVO();
    b.setId(144); // specified value - the db will honor this value
    b.setName("Cincinnati");
    // b.setCurrentCash(...); // unset - will use table default (0)
    rows = BranchDAO.insertByExample(b);
    Utilities.displayAllBranches("6. Branch inserted by example (identity PK specified). ID=" + b.getId()
        + ". Rows inserted=" + rows + "\n/// Updated list of visits ///");

  }

}