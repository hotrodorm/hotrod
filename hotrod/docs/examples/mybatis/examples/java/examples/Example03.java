package examples;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;

import daos.BranchDAO;
import daos.LogDAO;
import daos.VehicleDAO;

/**
 * Select By Example
 * 
 * @author valarcon
 *
 */
public class Example03 {

  public static void main(String[] args) throws SQLException {

    int rows;
    Timestamp now = new Timestamp(System.currentTimeMillis());

    // 1. Insert into a table with no PK

    LogDAO l = new LogDAO();
    l.setRecordedAt(now);
    l.setMessage("This is a log line.");
    rows = l.insert();
    System.out.println("Log line added. Rows inserted=" + rows);

    // 2. Insert into a table with identity-generated PK

    VehicleDAO skoda = new VehicleDAO();
    skoda.setBrand("Skoda");
    skoda.setModel("Octavia");
    skoda.setUsed(false);
    skoda.setCurrentMileage(7);
    skoda.setPurchasedOn(new Date(System.currentTimeMillis()));
    rows = skoda.insert();
    System.out.println("New vehicle Skoda added. New ID=" + skoda.getId() + ". Rows inserted=" + rows);

    // 3. Insert into a table with sequence-generated PK

    BranchDAO b = new BranchDAO();
    b.setName("Wichita");
    rows = b.insert();
    System.out.println("Branch added. New ID=" + b.getId() + ". Rows inserted=" + rows);

    // 4. Insert supplied PK for identity-generated PK
    // Note: this variant is only supported by DB2, and cannot run in H2.
    // See documentation for details.

  }

}
