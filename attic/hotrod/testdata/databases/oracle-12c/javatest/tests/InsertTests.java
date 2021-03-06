package tests;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Random;

import hotrod.test.generation.AccountVO;
import hotrod.test.generation.AgentVO;
import hotrod.test.generation.ConfigValuesVO;
import hotrod.test.generation.CursorExample1VO;
import hotrod.test.generation.TestSeqIdeDef1VO;
import hotrod.test.generation.TransactionVO;
import hotrod.test.generation.primitives.AccountDAO;
import hotrod.test.generation.primitives.AgentDAO;
import hotrod.test.generation.primitives.ConfigValuesDAO;
import hotrod.test.generation.primitives.CursorExample1DAO;
import hotrod.test.generation.primitives.TestSeqIdeDef1DAO;
import hotrod.test.generation.primitives.TransactionDAO;

public class InsertTests {

  public static void main(final String[] args) throws SQLException {
    // insertNoPK();
    // insertWithSequence();
    // insertWithIdentity();
    // insertWithOptionalIdentity();
    // insertWithSequenceIdentityDefault();
    // insertMixed();

    insertForCursorExample1();
  }

  private static void insertNoPK() throws SQLException {

    ConfigValuesVO example = new ConfigValuesVO();

    // ConfigValuesDAO.deleteByExample(example);

    ConfigValuesVO c = new ConfigValuesVO();
    Integer cell = getTimeInt() % 1000000;
    c.setNode(123);
    c.setCell(cell);
    c.setName("Cell #" + cell);
    c.setVerbatim("Local description");
    ConfigValuesDAO.insert(c);

    for (ConfigValuesVO l : ConfigValuesDAO.selectByExample(example)) {
      System.out.println("-> config=" + l);
    }

  }

  private static void insertForCursorExample1() throws SQLException {

    long total = 90 * 1000;

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

  private static void insertWithSequence() throws SQLException {

    AgentVO example = new AgentVO();
    // AgentDAO.deleteByExample(example);

    AgentVO a = new AgentVO();
    int time = getTimeInt() % 1000000;
    a.setName("Agent 007 - #" + time);
    a.setClientId(1001L);
    AgentDAO.insert(a);

    for (AgentVO l : AgentDAO.selectByExample(example)) {
      System.out.println("-> agent=" + l);
    }

  }

  private static void insertWithIdentity() throws SQLException {

    AccountVO example = new AccountVO();
    // AccountDAO.deleteByExample(example);

    AccountVO c = new AccountVO();
    int time = getTimeInt();
    c.setName("Account #" + time);
    c.setCurrentBalance(100);
    c.setType("CHK");
    c.setCreatedOn(new Timestamp(System.currentTimeMillis()));
    c.setRowVersion(1);
    AccountDAO.insert(c);

    System.out.println("============> ID=" + c.getId());

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
      t.setAccountId(3);
      t.setTime("time #" + time);
      t.setAmount(200);
      t.setFedBranchId(101L);
      TransactionDAO.insert(t);
      System.out.println("============> SEQ_ID=" + t.getSeqId());
    }

    { // with PK
      TransactionVO t = new TransactionVO();
      int time = getTimeInt() % 1000000;
      t.setAccountId(1);
      t.setSeqId(time);
      t.setTime("time #" + time);
      t.setAmount(300);
      t.setFedBranchId(102L);
      TransactionDAO.insert(t);
      System.out.println("============> SEQ_ID=" + t.getSeqId());
    }

    for (TransactionVO l : TransactionDAO.selectByExample(example)) {
      System.out.println("-> tx=" + l);
    }

  }

  private static void insertWithSequenceIdentityDefault() throws SQLException {
    TestSeqIdeDef1VO row = new TestSeqIdeDef1VO();
    row.setName("Toronto");
    row.setExtraId1(500);
    row.setExtraId2(600);
    TestSeqIdeDef1DAO.insert(row);

    System.out.println("row=" + row);
  }

  private static void insertMixed() throws SQLException {

    String time = getTime();
    int timeInt = getTimeInt();

    TestSeqIdeDef1VO example = new TestSeqIdeDef1VO();
    // AgentDAO.deleteByExample(example);

    TestSeqIdeDef1VO a = new TestSeqIdeDef1VO();
    a.setName("Caption 007 - " + time);
    a.setPrice(50004);
    // a.setBranchId(123456);

    // TestSeqIdeDef1DAO.insert(a);
    TestSeqIdeDef1DAO.insert(a, true);
    System.out.println("[inserted] mixed=" + a);

    // Optional Identity (default)

    // TestIdentity1VO ti1 = new TestIdentity1VO();
    // ti1.setName("Title (default) " + time);
    // TestIdentity1DAO.insert(ti1);
    // System.out.println("[inserted] optional identity=" + ti1);
    //
    // // Optional Identity (specified)
    //
    // TestIdentity1VO ti2 = new TestIdentity1VO();
    // ti2.setId(timeInt);
    // ti2.setName("Title (specified) " + time);
    // TestIdentity1DAO.insert(ti2);
    // System.out.println("[inserted] optional identity=" + ti2);

  }

  // Utilities

  private static int getTimeInt() {
    return (int) (System.currentTimeMillis() % (1000000000L));
  }

  private static String getTime() {
    return new SimpleDateFormat("HH:mm:ss").format(new java.util.Date());
  }

}
