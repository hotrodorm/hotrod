package tests;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Random;

import hotrod.test.generation.AccountVO;
import hotrod.test.generation.ConfigValuesVO;
import hotrod.test.generation.CursorExample1VO;
import hotrod.test.generation.TransactionVO;
import hotrod.test.generation.primitives.AccountDAO;
import hotrod.test.generation.primitives.ConfigValuesDAO;
import hotrod.test.generation.primitives.CursorExample1DAO;
import hotrod.test.generation.primitives.TransactionDAO;

public class InsertTests {

  public static void main(final String[] args) throws SQLException {
//    insertNoPK();
//    insertWithIdentity();
//    insertWithOptionalIdentity();
    insertForCursorExample1() ;
  }

  private static void insertNoPK() throws SQLException {

    ConfigValuesVO example = new ConfigValuesVO();

    // ConfigValuesDAO.deleteByExample(example);

    ConfigValuesVO c = new ConfigValuesVO();
    Integer cell = getTimeInt();
    c.setNode(123);
    c.setCell(cell);
    c.setName("Cell #" + cell);
    c.setVerbatim("Local description");
    ConfigValuesDAO.insert(c);

    for (ConfigValuesVO l : ConfigValuesDAO.selectByExample(example)) {
      System.out.println("-> config=" + l);
    }

  }

  private static void insertWithIdentity() throws SQLException {

    AccountVO example = new AccountVO();
    // AccountDAO.deleteByExample(example);

    AccountVO c = new AccountVO();
    int time = getTimeInt();
    c.setName("Account CHK #" + time);
    c.setCreatedOn(new Timestamp(System.currentTimeMillis()));
    c.setCurrentBalance(100);
    AccountDAO.insert(c);

    for (AccountVO l : AccountDAO.selectByExample(example)) {
      System.out.println("-> account=" + l);
    }

  }

  private static void insertWithOptionalIdentity() throws SQLException {

    TransactionVO example = new TransactionVO();
    // AccountDAO.deleteByExample(example);

    { // no PK
      TransactionVO t = new TransactionVO();
      int time = getTimeInt();
      t.setAccountId(1);
      t.setTime("time #" + time);
      t.setAmount(200);
      t.setFedBranchId(101);
      TransactionDAO.insert(t);
    }

    { // with PK
      TransactionVO t = new TransactionVO();
      int time = getTimeInt();
      t.setAccountId(1);
      t.setSeqId(time);
      t.setTime("time #" + time);
      t.setAmount(300);
      t.setFedBranchId(102);
      TransactionDAO.insert(t);
    }

    for (TransactionVO l : TransactionDAO.selectByExample(example)) {
      System.out.println("-> tx=" + l);
    }

  }
  
  // Cursor example

  private static void insertForCursorExample1() throws SQLException {

    long total = 100 * 1000;

    System.out.println("=== Will insert " + total + " for Cursor Example #1 ===");

    for (long i = 0; i < total; i++) {
      CursorExample1VO c = new CursorExample1VO();
      c.setId(i);
      c.setData(produceRandomString(1000));
      CursorExample1DAO.insert(c);
    }

    System.out.println("=== Inserted ===");

  }

  private static String produceRandomString(final int length) {
    int diff = '~' - ' ';
    Random random = new Random();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < length; i++) {
      char c = (char) (random.nextInt(diff) + (int) ' ');
      sb.append(c);
    }
    return sb.toString();
  }
  
  // Utilities

  private static int getTimeInt() {
    return (int) (System.currentTimeMillis() % ((long) Integer.MAX_VALUE));
  }

}
