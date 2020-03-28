package examples.crud2;

import java.sql.SQLException;

import daos.BranchVO;
import daos.ClientVO;
import daos.VehicleVO;
import daos.primitives.ClientDAO;
import daos.primitives.VehicleDAO;

/**
 * Example CRUD 206 - Select Parent By Foreign Key
 * 
 * @author Vladimir Alarcon
 * 
 */
public class ExampleCrud206 {

  public static void main(String[] args) throws SQLException {

    // 1. Select parent
    // Example: Get the parent branch row for a vehicle row, using the branch_id
    // FK that points to the branch table

    VehicleVO v = VehicleDAO.select(6); // get the Yamaha motorcycle
    BranchVO b = VehicleDAO.selectParentBranch().byBranchId(v);
    System.out.println("1. Select parent - the branch for the Yamaha motorcycle is: " + b.getName());

    // 2. Reflexive Select Parent
    // Example: Find out who referred the client "Gunilla"

    ClientVO c1 = ClientDAO.select(24); // Gunilla's ID
    ClientVO c2 = ClientDAO.selectParentClient().byReferredById(c1);
    System.out.println("2. Reflexive select Parent - Gunilla's referrer is: " + c2.getName());

  }

}
