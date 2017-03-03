package examples;

import java.sql.SQLException;
import java.util.List;

import daos.VehicleDAO;
import daos.primitives.VehicleDAOPrimitives.VehicleDAOOrderBy;

/**
 * Example 03 - Select by Example
 * 
 * @author valarcon
 * 
 */
public class Example03 {

  public static void main(String[] args) throws SQLException {

    System.out.println("=== Running Example 03 - Select by Example ===");
    VehicleDAO example;

    // 1. select by a single column - (type 'CAR')

    example = new VehicleDAO();
    example.setType("CAR");
    List<VehicleDAO> cars = VehicleDAO.selectByExample(example);
    Utilities.displayVehicles("1. Select All Vehicles of type 'CAR':", cars);

    // 2. select by multiple columns (BRAND & MODEL)

    example = new VehicleDAO();
    example.setBrand("Toyota");
    example.setModel("Tercel");
    List<VehicleDAO> tercel = VehicleDAO.selectByExample(example);
    Utilities.displayVehicles("2. Select All Vehicles 'Toyota Tercel':", tercel);

    // 3. select using null values (BRAND + MODEL + no BRANCH_ID)

    example = new VehicleDAO();
    example.setBrand("Toyota");
    example.setModel("Tercel");
    example.setBranchId(null); // This forces to search for null branch_id
    List<VehicleDAO> tercelNoBranch = VehicleDAO.selectByExample(example);
    Utilities.displayVehicles("3. Select All Vehicles 'Toyota Tercel' with no branch:", tercelNoBranch);

    // 4. Select unsold vehicles, with order
    // Sort ascending by type, then descending by brand, then ascending by model

    example = new VehicleDAO();
    example.setSold(false);
    List<VehicleDAO> unsold = VehicleDAO.selectByExample(example, VehicleDAOOrderBy.TYPE, VehicleDAOOrderBy.BRAND$DESC,
        VehicleDAOOrderBy.MODEL);
    Utilities.displayVehicles("4. Select unsold vehicles, with order:", unsold);

    // 5. Select unsold vehicles, with case insensitive order
    // Sort case-insensitive by brand, then case-insensitive by type descending

    example = new VehicleDAO();
    example.setSold(false);
    List<VehicleDAO> unsold2 = VehicleDAO.selectByExample(example, VehicleDAOOrderBy.BRAND$CASEINSENSITIVE,
        VehicleDAOOrderBy.TYPE$DESC_CASEINSENSITIVE);
    Utilities.displayVehicles("5. Select unsold vehicles, with case insensitive order:", unsold2);

    // 6. Select unsold vehicles, with case insensitive stable-forward order
    // Sort case-insensitive and stable-forward by brand

    example = new VehicleDAO();
    example.setSold(false);
    List<VehicleDAO> unsold3 = VehicleDAO.selectByExample(example,
        VehicleDAOOrderBy.BRAND$CASEINSENSITIVE_STABLE_FORWARD);
    Utilities.displayVehicles("6. Select unsold vehicles, with case insensitive stable-forward order:", unsold3);

    System.out.println(" ");
    System.out.println("=== Example 03 Complete ===");

  }

}
