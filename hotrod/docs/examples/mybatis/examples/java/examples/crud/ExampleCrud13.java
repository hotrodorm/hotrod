package examples.crud;

import java.sql.SQLException;

import daos.BranchVO;
import daos.ClientVO;
import daos.VehicleVO;
import daos.primitives.ClientDAO;
import daos.primitives.VehicleDAO;

/**
 * Example CRUD 13 - Select Parent By Foreign Key
 * 
 * @author Vladimir Alarcon
 * 
 */
public class ExampleCrud13 {

  public static void main(String[] args) throws SQLException {

    // 1. Navigate to parent
    // Example: Get the parent branch row for a vehicle row, using the branch_id
    // FK that points to the branch table

    VehicleVO v = VehicleDAO.select(6); // get the Yamaha motorcycle
    BranchVO b = VehicleDAO.selectParentBranch().byBranchId(v);
    System.out.println("1. Navigate to parent - the branch for the Yamaha motorcycle is: " + b.getName());

    // 2. Reflexive Navigate to Parent
    // Example: Find out who referred Gunilla

    ClientVO c1 = ClientDAO.select(24); // Gunilla's ID
    ClientVO p1 = ClientDAO.selectParentClient().byReferredById(c1);
    System.out.println("2. Reflexive Navigate to Parent - Gunilla's referrer is: " + p1.getName());

  }

}
