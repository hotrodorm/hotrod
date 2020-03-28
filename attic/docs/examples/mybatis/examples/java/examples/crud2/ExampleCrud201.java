package examples.crud2;

import java.sql.SQLException;
import java.util.List;

import daos.VehicleVO;
import daos.primitives.VehicleDAO;
import daos.primitives.VehicleDAO.VehicleOrderBy;
import examples.Utilities;

/**
 * Example CRUD 201 - Select By Example
 * 
 * @author Vladimir Alarcon
 * 
 */
public class ExampleCrud201 {

  public static void main(String[] args) throws SQLException {

    VehicleVO example;

    // 1. select by a single column - (type 'CAR')

    example = new VehicleVO();
    example.setType("CAR");
    List<VehicleVO> cars = VehicleDAO.selectByExample(example);
    Utilities.displayVehicles("1. Select All Vehicles of type 'CAR':", cars);

    // 2. select by multiple columns (BRAND & MODEL)

    example = new VehicleVO();
    example.setBrand("Toyota");
    example.setModel("Tercel");
    List<VehicleVO> tercel = VehicleDAO.selectByExample(example);
    Utilities.displayVehicles("2. Select All Vehicles 'Toyota Tercel':", tercel);

    // 3. select using null values (BRAND + MODEL + no BRANCH_ID)

    example = new VehicleVO();
    example.setBrand("Toyota");
    example.setModel("Tercel");
    example.setBranchId(null); // This forces to search for a null branch_id
    List<VehicleVO> tercelNoBranch = VehicleDAO.selectByExample(example);
    Utilities.displayVehicles("3. Select All Vehicles 'Toyota Tercel' with no branch:", tercelNoBranch);

    // 4. Select unsold vehicles, with order
    // Sort ascending by type, then descending by brand, then ascending by model

    example = new VehicleVO();
    example.setSold(false);
    List<VehicleVO> unsold = VehicleDAO.selectByExample(example, VehicleOrderBy.TYPE, VehicleOrderBy.BRAND$DESC,
        VehicleOrderBy.MODEL);
    Utilities.displayVehicles("4. Select unsold vehicles, with order:", unsold);

    // 5. Select unsold vehicles, with case insensitive order
    // Sort case-insensitive by brand, then case-insensitive by type descending

    example = new VehicleVO();
    example.setSold(false);
    List<VehicleVO> unsold2 = VehicleDAO.selectByExample(example, VehicleOrderBy.BRAND$CASEINSENSITIVE,
        VehicleOrderBy.TYPE$DESC_CASEINSENSITIVE);
    Utilities.displayVehicles("5. Select unsold vehicles, with case insensitive order:", unsold2);

    // 6. Select unsold vehicles, with case insensitive stable-forward order
    // Sort case-insensitive and stable-forward by brand

    example = new VehicleVO();
    example.setSold(false);
    List<VehicleVO> unsold3 = VehicleDAO.selectByExample(example, VehicleOrderBy.BRAND$CASEINSENSITIVE_STABLE_FORWARD);
    Utilities.displayVehicles("6. Select unsold vehicles, with case insensitive stable-forward order:", unsold3);

  }

}
