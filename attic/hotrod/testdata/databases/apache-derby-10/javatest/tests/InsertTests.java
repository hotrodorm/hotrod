package tests;

import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class InsertTests {

  public static void main(final String[] args) throws SQLException {
    // insertNoPK();
    // insertWithSequence();
    // insertWithIdentity();
    // insertWithOptionalIdentity();
//    insertMixed();
  }

//  private static void insertNoPK() throws SQLException {
//
//    ConfigValuesVO example = new ConfigValuesVO();
//    // ConfigValuesDAO.deleteByExample(example);
//
//    ConfigValuesVO c = new ConfigValuesVO();
//    Integer cell = getTimeInt();
//    c.setNode(123);
//    c.setCell(cell);
//    c.setName("Cell #" + cell);
//    c.setVerbatim("Local description");
//    ConfigValuesDAO.insert(c);
//
//    for (ConfigValuesVO l : ConfigValuesDAO.selectByExample(example)) {
//      System.out.println("-> config=" + l);
//    }
//
//  }
//
//  private static void insertWithSequence() throws SQLException {
//
//    AgentVO example = new AgentVO();
//    // AgentDAO.deleteByExample(example);
//
//    AgentVO a = new AgentVO();
//    int time = getTimeInt();
//    a.setName("Agent 007 - #" + time);
//    a.setClientId(1001L);
//    AgentDAO.insert(a);
//    System.out.println("[inserted] agentId=" + a.getId());
//
//    for (AgentVO l : AgentDAO.selectByExample(example)) {
//      System.out.println("-> agent=" + l);
//    }
//
//  }
//
//  private static void insertWithIdentity() throws SQLException {
//
//    AccountVO example = new AccountVO();
//    // AccountDAO.deleteByExample(example);
//
//    AccountVO c = new AccountVO();
//    int time = getTimeInt();
//    c.setName("Acc #" + time);
//    c.setType("CHK");
//    c.setCreatedOn(new java.sql.Date(System.currentTimeMillis()));
//    c.setCurrentBalance(100);
//    AccountDAO.insert(c);
//    System.out.println("[inserted] id=" + c.getId());
//
//    for (AccountVO l : AccountDAO.selectByExample(example)) {
//      System.out.println("-> account=" + l);
//    }
//
//  }
//
//  private static void insertWithOptionalIdentity() throws SQLException {
//
//    TransactionVO example = new TransactionVO();
//    // AccountDAO.deleteByExample(example);
//
//    { // no PK
//      TransactionVO t = new TransactionVO();
//      int time = getTimeInt();
//      t.setAccountId(1);
//      t.setTime("time #" + time);
//      t.setAmount(200);
//      t.setFedBranchId(101);
//      System.out.println("[before inserted] t=" + t);
//      TransactionDAO.insert(t);
//      System.out.println("[inserted] seqId=" + t.getSeqId());
//    }
//
//    { // with PK
//      TransactionVO t = new TransactionVO();
//      int time = getTimeInt();
//      t.setAccountId(1);
//      t.setSeqId(time);
//      t.setTime("time #" + time);
//      t.setAmount(300);
//      t.setFedBranchId(102);
//      System.out.println("[before inserted] t=" + t);
//      TransactionDAO.insert(t);
//      System.out.println("[inserted] seqId=" + t.getSeqId());
//    }
//
//    for (TransactionVO l : TransactionDAO.selectByExample(example)) {
//      System.out.println("-> tx=" + l);
//    }
//
//  }
//
//  private static void insertMixed() throws SQLException {
//
//    String time = getTime();
//    int timeInt = getTimeInt();
//
//    TestSeqIdeDef1VO example = new TestSeqIdeDef1VO();
//    // AgentDAO.deleteByExample(example);
//
//    TestSeqIdeDef1VO a = new TestSeqIdeDef1VO();
//    a.setName("Caption 007 - " + time);
//    a.setPrice(50004);
//    // a.setBranchId(123456);
//
//    TestSeqIdeDef1DAO.insert(a);
//    System.out.println("[inserted] mixed=" + a);
//
//    // // Optional Identity (default)
//    //
//    // TestIdentity1VO ti1 = new TestIdentity1VO();
//    // ti1.setName("Title (default) " + time);
//    // TestIdentity1DAO.insert(ti1);
//    // System.out.println("[inserted] optional identity=" + ti1);
//    //
//    // // Optional Identity (specified)
//    //
//    // TestIdentity1VO ti2 = new TestIdentity1VO();
//    // ti2.setId(timeInt);
//    // ti2.setName("Title (specified) " + time);
//    // TestIdentity1DAO.insert(ti2);
//    // System.out.println("[inserted] optional identity=" + ti2);
//
//  }

  // Utilities

  @SuppressWarnings("unused")
  private static int getTimeInt() {
    return (int) (System.currentTimeMillis() % ((long) Integer.MAX_VALUE));
  }

  @SuppressWarnings("unused")
  private static String getTime() {
    return new SimpleDateFormat("HH:mm:ss").format(new java.util.Date());
  }

}
