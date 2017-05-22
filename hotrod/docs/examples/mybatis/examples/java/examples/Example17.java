package examples;

import java.sql.Date;
import java.sql.SQLException;

import daos.VehicleValuationDAO;

/**
 * Example 17 - Custom Class Names for DAOs
 * 
 * @author Vladimir Alarcon
 * 
 */
public class Example17 {

  public static void main(String[] args) throws SQLException {

    System.out.println("=== Running Example 17 - Custom Class Names for DAOs ===");

    // 1. Insert using an alias name in Java
    // Example: the table MT_VVAL002 is managed as the Java DAO
    // VehicleValuationDAO. See the hotrod.xml configuration file to see the
    // SQL-Java name equivalence.

    Date closingDate = Date.valueOf("2017-02-14");
    VehicleValuationDAO v = new VehicleValuationDAO(); // MT_VVAL002 table
    v.setValuationDate(closingDate);
    v.setBranchId(104);
    v.setTotalBranchValuation(156780.0);
    v.insert(); // inserts into the MT_VVAL002 table!
    System.out.println(" ");
    System.out.println("1. Insert using an alias name in Java.");

    System.out.println(" ");
    System.out.println("=== Example 17 Complete ===");

  }

}
