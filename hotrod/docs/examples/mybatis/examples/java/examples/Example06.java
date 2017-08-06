package examples;

import java.sql.SQLException;
import java.util.List;

import daos.BranchVO;
import daos.ClientVO;
import daos.VehicleVO;
import daos.primitives.BranchDAO;
import daos.primitives.ClientDAO;
import daos.primitives.ClientDAO.ClientOrderBy;
import daos.primitives.VehicleDAO;
import daos.primitives.VehicleDAO.VehicleOrderBy;

/**
 * Example 06 - Navigating Foreign Keys
 * 
 * @author Vladimir Alarcon
 * 
 */
public class Example06 {

  public static void main(String[] args) throws SQLException {

    System.out.println("=== Running Example 06 - Navigating Foreign Keys ===");

    // 1. Navigate to parent
    // Example: Get the parent branch row for a vehicle row, using the branch_id
    // FK that points to the branch table

    VehicleVO v = VehicleDAO.select(6); // get the Yamaha motorcycle
    BranchVO b = VehicleDAO.selectParentBranch().byBranchId(v);
    System.out.println("1. Navigate to parent - the branch for the Yamaha motorcycle is: " + b.getName());

    // 2. Navigate to Children
    // Example: Get all the vehicles for a branch 101

    BranchVO b2 = BranchDAO.select(101);
    List<VehicleVO> vehicles2 = BranchDAO.selectChildrenVehicle().byBranchId(b2);
    Utilities.displayVehicles("2. Navigate to Children:", vehicles2);

    // 3. Navigate to Children With Ordering
    // Example: Get all the vehicles for a branch row, order by brand, then
    // purchased_on descending

    BranchVO b3 = BranchDAO.select(101);
    List<VehicleVO> vehicles3 = BranchDAO.selectChildrenVehicle().byBranchId(b3, VehicleOrderBy.BRAND,
        VehicleOrderBy.PURCHASED_ON$DESC);
    Utilities.displayVehicles("3. Navigate to Children With Ordering:", vehicles3);

    // 4. Reflexive Navigate to Parent
    // Example: Find out who referred Gunilla

    ClientVO c1 = ClientDAO.select(24); // Gunilla's ID
    ClientVO p1 = ClientDAO.selectParentClient().byReferredById(c1);
    System.out.println(" ");
    System.out.println("4. Reflexive Navigate to Parent - Gunilla's referrer is: " + p1.getName());

    // 5. Reflexive Navigate to Children
    // Example: Find out who has been referred by Jane

    ClientVO c2 = ClientDAO.select(21); // Jane's ID
    List<ClientVO> clients2 = ClientDAO.selectChildrenClient().byReferredById(c2);
    Utilities.displayClients("5. Reflexive Navigate to Children:", clients2);

    // 6. Reflexive Navigate to Children With Order
    // Example: Find out who has been referred by Jane, ordered by creation date
    // descending, then by name (ascending, case insensitive).

    ClientVO c3 = ClientDAO.select(21); // Jane's ID
    List<ClientVO> clients3 = ClientDAO.selectChildrenClient().byReferredById(c3, ClientOrderBy.CREATED_AT$DESC,
        ClientOrderBy.NAME$CASEINSENSITIVE);
    Utilities.displayClients("6. Reflexive Navigate to Children With Order:", clients3);

    System.out.println(" ");
    System.out.println("=== Example 06 Complete ===");

  }

}
