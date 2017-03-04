package examples;

import java.sql.Date;
import java.sql.SQLException;

import daos.VehicleValuationDAO;

/**
 * Example 18 - Custom Property Names for DAOs
 * 
 * @author valarcon
 * 
 */
public class Example18 {

  public static void main(String[] args) throws SQLException {

    System.out.println("=== Running Example 18 - Custom Property Names for DAOs ===");

    // 1. Insert using alias names for properties in Java
    // Example: the table MT_VVAL002 is managed as the Java DAO
    // VehicleValuationDAO. See the hotrod.xml configuration file to see the
    // SQL-Java property names equivalence.

    // Column "IF"
    // -----------
    // If no alias is used for the first column "IF", it would generate the java
    // property name "if". This is actually a reserved word in Java and,
    // therefore, the DAO source code would be invalid. In this case the
    // property MUST be given a java name.

    // Column "SLDBRID"
    // ----------------
    // This column would generate a valid Java property name. However, it's
    // somewhat confusing legacy name. It's given a java name "branchId" that is
    // far more easy to use and understand.

    // Column "$valuation"
    // -------------------
    // This column has a special non-alphanumeric character in its name. This
    // can cause all kinds of confusion in the Java source code and for MyBatis
    // as well. It's better to give it an easier java name: totalBranchValuation

    Date closingDate = Date.valueOf("2017-02-14");
    VehicleValuationDAO v = new VehicleValuationDAO();
    v.setValuationDate(closingDate); // column IF!
    v.setBranchId(104); // column SLDBRID!
    v.setTotalBranchValuation(156780.0); // column $valuation!
    v.insert(); 
    System.out.println(" ");
    System.out.println("1. Insert using alias names for properties in Java.");

    System.out.println(" ");
    System.out.println("=== Example 18 Complete ===");

  }

}
