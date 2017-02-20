package tests;

import java.sql.SQLException;
import java.sql.Timestamp;

import hotrod.test.generation.AccountDAO;
import hotrod.test.generation.ConfigValuesDAO;

public class InsertTests {

  public static void main(final String[] args) throws SQLException {
//    insertNoPK();
    insertWithIdentity();
  }

  private static void insertNoPK() throws SQLException {

    ConfigValuesDAO example = new ConfigValuesDAO();

    // ConfigValuesDAO.deleteByExample(example);

    ConfigValuesDAO c = new ConfigValuesDAO();
    Integer cell = getTimeInt();
    c.setNode(123);
    c.setCell(cell);
    c.setName("Cell #" + cell);
    c.setVerbatim("Local description");
    c.insert();

    for (ConfigValuesDAO l : ConfigValuesDAO.selectByExample(example)) {
      System.out.println("-> config=" + l);
    }

  }

  private static void insertWithIdentity() throws SQLException {

    AccountDAO example = new AccountDAO();
    // AccountDAO.deleteByExample(example);

    AccountDAO c = new AccountDAO();
    int time = getTimeInt();
    c.setName("Account CHK #" + time);
    c.setCreatedOn(new Timestamp(System.currentTimeMillis()));
    c.setCurrentBalance(100);
    c.insert();

    for (AccountDAO l : AccountDAO.selectByExample(example)) {
      System.out.println("-> account=" + l);
    }

  }

  // Utilities

  private static int getTimeInt() {
    return (int) (System.currentTimeMillis() % ((long) Integer.MAX_VALUE));
  }

}
