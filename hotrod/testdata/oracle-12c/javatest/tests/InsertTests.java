package tests;

import java.sql.SQLException;
import java.sql.Timestamp;

import hotrod.test.generation.AccountDAO;
import hotrod.test.generation.AgentDAO;
import hotrod.test.generation.ConfigValuesDAO;
import hotrod.test.generation.TransactionDAO;

public class InsertTests {

  public static void main(final String[] args) throws SQLException {
    // insertNoPK();
    // insertWithSequence();
    // insertWithIdentity();
    insertWithOptionalIdentity();
  }

  private static void insertNoPK() throws SQLException {

    ConfigValuesDAO example = new ConfigValuesDAO();

    // ConfigValuesDAO.deleteByExample(example);

    ConfigValuesDAO c = new ConfigValuesDAO();
    Integer cell = getTimeInt() % 1000000;
    c.setNode(123);
    c.setCell(cell);
    c.setName("Cell #" + cell);
    c.setVerbatim("Local description");
    c.insert();

    for (ConfigValuesDAO l : ConfigValuesDAO.selectByExample(example)) {
      System.out.println("-> config=" + l);
    }

  }

  private static void insertWithSequence() throws SQLException {

    AgentDAO example = new AgentDAO();
    // AgentDAO.deleteByExample(example);

    AgentDAO a = new AgentDAO();
    int time = getTimeInt() % 1000000;
    a.setName("Agent 007 - #" + time);
    a.setClientId(1001L);
    a.insert();

    for (AgentDAO l : AgentDAO.selectByExample(example)) {
      System.out.println("-> agent=" + l);
    }

  }

  private static void insertWithIdentity() throws SQLException {

    AccountDAO example = new AccountDAO();
    // AccountDAO.deleteByExample(example);

    AccountDAO c = new AccountDAO();
    int time = getTimeInt();
    c.setName("Account #" + time);
    c.setCurrentBalance(100);
    c.setType("CHK");
    c.setCreatedOn(new Timestamp(System.currentTimeMillis()));
    c.setRowVersion(1);
    c.insert();

    System.out.println("============> ID=" + c.getId());

    for (AccountDAO l : AccountDAO.selectByExample(example)) {
      System.out.println("-> account=" + l);
    }

  }

  private static void insertWithOptionalIdentity() throws SQLException {

    TransactionDAO example = new TransactionDAO();
    // AccountDAO.deleteByExample(example);

    { // no PK
      TransactionDAO t = new TransactionDAO();
      int time = getTimeInt();
      t.setAccountId(3);
      t.setTime("time #" + time);
      t.setAmount(200);
      t.setFedBranchId(101L);
      t.insert();
      System.out.println("============> SEQ_ID=" + t.getSeqId());
    }

    { // with PK
      TransactionDAO t = new TransactionDAO();
      int time = getTimeInt() % 1000000;
      t.setAccountId(1);
      t.setSeqId(time);
      t.setTime("time #" + time);
      t.setAmount(300);
      t.setFedBranchId(102L);
      t.insert();
      System.out.println("============> SEQ_ID=" + t.getSeqId());
    }

    for (TransactionDAO l : TransactionDAO.selectByExample(example)) {
      System.out.println("-> tx=" + l);
    }

  }

  // Utilities

  private static int getTimeInt() {
    return (int) (System.currentTimeMillis() % ((long) Integer.MAX_VALUE));
  }

}
