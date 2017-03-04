package examples;

import java.sql.Date;
import java.sql.SQLException;

import daos.VehicleValuationDAO;

/**
 * Example 19 - Custom Property Java Types for DAOs
 * 
 * @author valarcon
 * 
 */
public class Example19 {

  public static void main(String[] args) throws SQLException {

    System.out.println("=== Running Example 19 - Custom Property Java Types for DAOs ===");

    // 1. Insert using custom property Java types

    // The table column "$valuation" used in Java as totalBranchValuation is
    // defined using the custom Java type java.lang.Double (See the hotrod.xml
    // configuration file). This overrides the Java type (java.math.BigDecimal)
    // provided by default for this database column type.

    Date closingDate = Date.valueOf("2017-02-14");
    VehicleValuationDAO v = new VehicleValuationDAO();
    v.setValuationDate(closingDate);
    v.setBranchId(104);
    v.setTotalBranchValuation(156780.0); // It's a Double value!
    v.insert();
    System.out.println(" ");
    System.out.println("1. Insert using custom property Java types.");

    System.out.println(" ");
    System.out.println("=== Example 19 Complete ===");

  }

}
