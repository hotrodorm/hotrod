package examples;

import java.sql.SQLException;
import java.util.List;

import daos.BranchDAO;
import daos.ClientDAO;
import daos.VehicleDAO;
import daos.primitives.ClientDAOPrimitives.ClientDAOOrderBy;
import daos.primitives.VehicleDAOPrimitives.VehicleDAOOrderBy;

/**
 * Example 06 - Navigating Foreign Keys
 * 
 * @author valarcon
 * 
 */
public class Example06 {

  public static void main(String[] args) throws SQLException {

    System.out.println("=== Running Example 06 - Navigating Foreign Keys ===");

    // 1. Navigate to parent
    // Example: Get the parent branch row for a vehicle row, using the branch_id
    // FK that points to the branch table

    VehicleDAO v = VehicleDAO.select(6); // get the Yamaha motorcycle
    BranchDAO b = v.selectParentBranchDAO().byBranchId();
    System.out.println("1. Navigate to parent - the branch for the Yamaha motorcycle is: " + b.getName());

    // 2. Navigate to Children
    // Example: Get all the vehicles for a branch 101

    BranchDAO b2 = BranchDAO.select(101);
    List<VehicleDAO> vehicles2 = b2.selectChildrenVehicleDAO().byBranchId();
    Utilities.displayVehicles("2. Navigate to Children:", vehicles2);

    // 3. Navigate to Children With Ordering
    // Example: Get all the vehicles for a branch row, order by brand, then
    // purchased_on descending

    BranchDAO b3 = BranchDAO.select(101);
    List<VehicleDAO> vehicles3 = b3.selectChildrenVehicleDAO().byBranchId(VehicleDAOOrderBy.BRAND,
        VehicleDAOOrderBy.PURCHASED_ON$DESC);
    Utilities.displayVehicles("3. Navigate to Children With Ordering:", vehicles3);

    // 4. Reflexive Navigate to Parent
    // Example: Find out who referred Gunilla

    ClientDAO c1 = ClientDAO.select(24); // Gunilla's ID
    ClientDAO p1 = c1.selectParentClientDAO().byReferredById();
    System.out.println(" ");
    System.out.println("4. Reflexive Navigate to Parent - Gunilla's referrer is: " + p1.getName());

    // 5. Reflexive Navigate to Children
    // Example: Find out who has been referred by Jane

    ClientDAO c2 = ClientDAO.select(21); // Jane's ID
    List<ClientDAO> clients2 = c2.selectChildrenClientDAO().byReferredById();
    Utilities.displayClients("5. Reflexive Navigate to Children:", clients2);

    // 6. Reflexive Navigate to Children With Order
    // Example: Find out who has been referred by Jane, ordered by creation date
    // descending, then by name (ascending, case insensitive).

    ClientDAO c3 = ClientDAO.select(21); // Jane's ID
    List<ClientDAO> clients3 = c3.selectChildrenClientDAO().byReferredById(ClientDAOOrderBy.CREATED_AT$DESC,
        ClientDAOOrderBy.NAME$CASEINSENSITIVE);
    Utilities.displayClients("6. Reflexive Navigate to Children With Order:", clients3);

    System.out.println(" ");
    System.out.println("=== Example 06 Complete ===");

  }

}
