package tests;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import hotrod.test.generation.AccountVO;
import hotrod.test.generation.ConfigValuesVO;
import hotrod.test.generation.TestIdentity1VO;
import hotrod.test.generation.TransactionVO;
import hotrod.test.generation.primitives.AccountDAO;
import hotrod.test.generation.primitives.ConfigValuesDAO;
import hotrod.test.generation.primitives.TestIdentity1DAO;
import hotrod.test.generation.primitives.TransactionDAO;

public class InsertTests {

  public static void main(final String[] args) throws SQLException {

    // insertNoPK();
    // insertWithIdentity();
    // insertWithOptionalIdentity();

    // insertByExampleNoPK();
    // insertByExampleWithIdentity();
    // insertByExampleWithOptionalIdentity();
    insertMixed();

  }

  // ==============
  // NORMAL INSERTS
  // ==============

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

  // =================
  // INSERT BY EXAMPLE
  // =================

  private static void insertByExampleNoPK() throws SQLException {

    ConfigValuesVO example = new ConfigValuesVO();

    // ConfigValuesDAO.deleteByExample(example);

    ConfigValuesVO c = new ConfigValuesVO();
    Integer cell = getTimeInt();
    c.setNode(123);
    c.setCell(cell);
    c.setName("Cell #" + cell);
    // c.setVerbatim("Local description"); // unset!
    ConfigValuesDAO.insertByExample(c);

    for (ConfigValuesVO l : ConfigValuesDAO.selectByExample(example)) {
      System.out.println("-> config=" + l);
    }

  }

  private static void insertByExampleWithIdentity() throws SQLException {

    AccountVO example = new AccountVO();
    // AccountDAO.deleteByExample(example);

    AccountVO c = new AccountVO();
    int time = getTimeInt();
    // c.setName("Account CHK #" + time); // unset!
    c.setCreatedOn(new Timestamp(System.currentTimeMillis()));
    c.setCurrentBalance(100);
    AccountDAO.insertByExample(c);

    for (AccountVO l : AccountDAO.selectByExample(example)) {
      System.out.println("-> account=" + l);
    }

  }

  private static void insertByExampleWithOptionalIdentity() throws SQLException {

    TransactionVO example = new TransactionVO();
    // AccountDAO.deleteByExample(example);

    { // no PK
      TransactionVO t = new TransactionVO();
      int time = getTimeInt();
      t.setAccountId(1);
      t.setTime("time #" + time);
      // t.setAmount(200); // unset!
      t.setFedBranchId(101);
      TransactionDAO.insertByExample(t);
    }

    { // with PK
      TransactionVO t = new TransactionVO();
      int time = getTimeInt();
      t.setAccountId(1);
      t.setSeqId(time);
      t.setTime("time #" + time);
      // t.setAmount(300); // unset!
      t.setFedBranchId(102);
      TransactionDAO.insertByExample(t);
    }

    for (TransactionVO l : TransactionDAO.selectByExample(example)) {
      System.out.println("-> tx=" + l);
    }

  }

  private static void insertMixed() throws SQLException {

    String time = getTime();
    int timeInt = getTimeInt();

    // Optional Identity (default)

    TestIdentity1VO ti1 = new TestIdentity1VO();
    ti1.setName("Title (default) " + time);
    TestIdentity1DAO.insert(ti1);
    System.out.println("[inserted] optional identity=" + ti1);

    // Optional Identity (specified)

    TestIdentity1VO ti2 = new TestIdentity1VO();
    ti2.setId(timeInt);
    ti2.setName("Title (specified) " + time);
    TestIdentity1DAO.insert(ti2);
    System.out.println("[inserted] optional identity=" + ti2);

  }

  // Utilities

  private static int getTimeInt() {
    return (int) (System.currentTimeMillis() % (1000000000L));
  }

  private static String getTime() {
    return new SimpleDateFormat("HH:mm:ss").format(new java.util.Date());
  }

}
