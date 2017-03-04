package examples;

import java.sql.SQLException;
import java.sql.Timestamp;

import org.hotrod.runtime.exceptions.StaleDataException;

import daos.ClientDAO;

/**
 * Example 16 - Optimistic Locking (Row Version Control)
 * 
 * @author valarcon
 * 
 */
public class Example16 {

  public static void main(String[] args) throws SQLException {

    System.out.println("=== Running Example 16 - Optimistic Locking (Row Version Control) ===");

    Timestamp now = new Timestamp(System.currentTimeMillis());

    // ----------------------------------
    // 1. Insert with row version control
    // ----------------------------------

    System.out.println(" ");
    ClientDAO c = new ClientDAO();
    c.setCreatedAt(now);
    c.setName("John Silver");
    c.setState("WA");
    c.setDriversLicense("WT5429-3342");
    c.setReferredById(null);
    c.setTotalPurchased(0L);
    c.setVip(false);
    c.setRowVersion(12345L); // Ignored. Set to zero on insert
    c.insert();
    Integer id = c.getId();
    System.out.println("1. Insert with row version control.");

    // ---------------------------------------------------
    // 2. Update with row version control - No concurrency
    // ---------------------------------------------------

    System.out.println(" ");
    c.setReferredById(22);
    try {
      c.update(); // should succeed
      // Update succeeded; row hadn't been updated by another process.
      System.out.println("2. Update with row version control - Succeeded (expected behavior)");
    } catch (StaleDataException e) {
      // Update failed; row had been updated by another process.
      System.out.println("2. Update with row version control - Failed (should not happen)");
    }

    // -----------------------------------------------------
    // 3. Update with row version control - With concurrency
    // -----------------------------------------------------

    System.out.println(" ");

    // 3.1. Meanwhile the row is updated (could be another process or thread).
    ClientDAO example = new ClientDAO();
    example.setId(id);
    ClientDAO updateValues = new ClientDAO();
    updateValues.setDriversLicense("12345-67890");
    ClientDAO.updateByExample(example, updateValues); // always succeeds and
                                                      // increments the version

    // 3.2. Now we try to update the original row
    c.setState("NY");
    try {
      c.update(); // should fail
      System.out.println("3. Update with row version control (with concurrency) - Succeeded (should not happen)");
    } catch (StaleDataException e) {
      System.out.println("3. Update with row version control (with concurrency) - Failed (expected behavior)");
    }

    System.out.println(" ");
    System.out.println("=== Example 16 Complete ===");

  }

}
