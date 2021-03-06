package examples;

import java.sql.SQLException;
import java.sql.Timestamp;

import org.hotrod.runtime.exceptions.StaleDataException;

import daos.ClientVO;
import daos.primitives.ClientDAO;

/**
 * Example 16 - Optimistic Locking (Row Version Control)
 * 
 * @author Vladimir Alarcon
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
    ClientVO c = new ClientVO();
    c.setCreatedAt(now);
    c.setName("John Silver");
    c.setState("WA");
    c.setDriversLicense("WT5429-3342");
    c.setReferredById(null);
    c.setTotalPurchased(0L);
    c.setVip(false);
    c.setRowVersion(12345L); // Ignored. Set to zero on insert
    ClientDAO.insert(c);
    Integer id = c.getId();
    System.out.println("1. Insert with row version control.");

    // ---------------------------------------------------
    // 2. Update with row version control - No concurrency
    // ---------------------------------------------------

    System.out.println(" ");
    c.setReferredById(22);
    try {
      ClientDAO.update(c); // should succeed
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
    ClientVO example = new ClientVO();
    example.setId(id);
    ClientVO updateValues = new ClientVO();
    updateValues.setDriversLicense("12345-67890");
    ClientDAO.updateByExample(example, updateValues); // always succeeds and
                                                      // increments the version

    // 3.2. Now we try to update the original row
    c.setState("NY");
    try {
      ClientDAO.update(c); // should fail
      System.out.println("3. Update with row version control (with concurrency) - Succeeded (should not happen)");
    } catch (StaleDataException e) {
      System.out.println("3. Update with row version control (with concurrency) - Failed (expected behavior)");
    }

    System.out.println(" ");
    System.out.println("=== Example 16 Complete ===");

  }

}
