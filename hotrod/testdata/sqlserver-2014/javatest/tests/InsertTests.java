package tests;

import java.sql.SQLException;
import java.sql.Timestamp;

import hotrod.test.generation.AccountVO;
import hotrod.test.generation.AgentVO;
import hotrod.test.generation.ConfigValuesVO;
import hotrod.test.generation.primitives.AccountDAO;
import hotrod.test.generation.primitives.AgentDAO;
import hotrod.test.generation.primitives.ConfigValuesDAO;

public class InsertTests {

  public static void main(final String[] args) throws SQLException {
    insertNoPK();
    insertWithSequence();
    insertWithIdentity();
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

  private static void insertWithSequence() throws SQLException {

    AgentVO example = new AgentVO();
    // AgentDAO.deleteByExample(example);

    AgentVO a = new AgentVO();
    int time = getTimeInt();
    a.setName("Agent 007 - #" + time);
    a.setClientId(12L);
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
    c.setCreatedOn(new Timestamp(System.currentTimeMillis()));
    c.setCurrentBalance(100);
    AccountDAO.insert(c);

    for (AccountVO l : AccountDAO.selectByExample(example)) {
      System.out.println("-> account=" + l);
    }

  }

  // Utilities

  private static int getTimeInt() {
    return (int) (System.currentTimeMillis() % ((long) Integer.MAX_VALUE));
  }

}
