package tests;

import java.sql.SQLException;
import java.sql.Timestamp;

import hotrod.test.generation.AccountDAO;
import hotrod.test.generation.AgentDAO;
import hotrod.test.generation.ConfigValuesDAO;

public class InsertTests {

  public static void main(final String[] args) throws SQLException {
    insertNoPK();
    insertWithSequence();
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

  private static void insertWithSequence() throws SQLException {

    AgentDAO example = new AgentDAO();
    // AgentDAO.deleteByExample(example);

    AgentDAO a = new AgentDAO();
    int time = getTimeInt();
    a.setName("Agent 007 - #" + time);
    a.setClientId(12L);
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
