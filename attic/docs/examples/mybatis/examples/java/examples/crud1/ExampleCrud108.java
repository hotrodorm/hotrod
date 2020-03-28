package examples.crud1;

import java.sql.SQLException;

import daos.BranchVO;
import daos.primitives.BranchDAO;

/**
 * Example CRUD 108 - Insert using Optional Identity
 * 
 * @author Vladimir Alarcon
 * 
 */
public class ExampleCrud108 {

  public static void main(String[] args) throws SQLException {

    // a) Insert using into a table with optional identity PK (not specified)

    BranchVO b = new BranchVO();
    b.setId(null); // null: the DB will generate the PK
    b.setName("Wichita");
    b.setCurrentCash(0);
    int rows = BranchDAO.insert(b);
    System.out.println("a) Branch inserted (identity PK not specified). ID=" + b.getId() + ". Rows inserted=" + rows);

    // b) Insert using into a table with optional identity PK (specified)

    b = new BranchVO();
    b.setId(144); // specified value: this value will be used for the PK
    b.setName("Cincinnati");
    b.setCurrentCash(0);
    rows = BranchDAO.insert(b);
    System.out.println("b) Branch inserted (identity PK specified). ID=" + b.getId() + ". Rows inserted=" + rows);

  }

}
