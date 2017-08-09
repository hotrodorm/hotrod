package examples.crud;

import java.sql.SQLException;
import java.util.List;

import daos.BranchVO;
import daos.ClientVO;
import daos.VehicleVO;
import daos.primitives.BranchDAO;
import daos.primitives.ClientDAO;
import daos.primitives.ClientDAO.ClientOrderBy;
import daos.primitives.VehicleDAO.VehicleOrderBy;
import examples.Utilities;

/**
 * Example CRUD 13 - Select Children By Foreign Key
 * 
 * @author Vladimir Alarcon
 * 
 */
public class ExampleCrud14 {

  public static void main(String[] args) throws SQLException {

    // 1. Navigate to Children
    // Example: Get all the vehicles for a branch 101

    BranchVO b2 = BranchDAO.select(101);
    List<VehicleVO> vehicles2 = BranchDAO.selectChildrenVehicle().byBranchId(b2);
    Utilities.displayVehicles("1. Navigate to Children:", vehicles2);

    // 2. Navigate to Children With Ordering
    // Example: Get all the vehicles for a branch row, order by brand, then
    // purchased_on descending

    BranchVO b3 = BranchDAO.select(101);
    List<VehicleVO> vehicles3 = BranchDAO.selectChildrenVehicle().byBranchId(b3, VehicleOrderBy.BRAND,
        VehicleOrderBy.PURCHASED_ON$DESC);
    Utilities.displayVehicles("2. Navigate to Children With Ordering:", vehicles3);

    // 3. Reflexive Navigate to Children
    // Example: Find out who has been referred by Jane

    ClientVO c2 = ClientDAO.select(21); // Jane's ID
    List<ClientVO> clients2 = ClientDAO.selectChildrenClient().byReferredById(c2);
    Utilities.displayClients("3. Reflexive Navigate to Children:", clients2);

    // 4. Reflexive Navigate to Children With Order
    // Example: Find out who has been referred by Jane, ordered by creation date
    // descending, then by name (ascending, case insensitive).

    ClientVO c3 = ClientDAO.select(21); // Jane's ID
    List<ClientVO> clients3 = ClientDAO.selectChildrenClient().byReferredById(c3, ClientOrderBy.CREATED_AT$DESC,
        ClientOrderBy.NAME$CASEINSENSITIVE);
    Utilities.displayClients("4. Reflexive Navigate to Children With Order:", clients3);

  }

}
