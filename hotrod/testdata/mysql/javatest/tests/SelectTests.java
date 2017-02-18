package tests;

import java.io.IOException;
import java.sql.SQLException;

import hotrod.test.generation.AccountTx0DAO;
import hotrod.test.generation.AccountTx1DAO;
import hotrod.test.generation.BigAccountTxDAO;
import hotrod.test.generation.ConfigValuesDAO;
import hotrod.test.generation.MultParamSelectDAO;

public class SelectTests {

  public static void main(final String[] args) throws IOException, SQLException {
    countProperties();
  }

  private static void countProperties() throws SQLException {

    selectByExample();
    // selectByUI();

  }

  private static void selectByExample() throws SQLException {

    {
      System.out.println("--- a0 ---");
      for (AccountTx0DAO a0 : AccountTx0DAO.select()) {
        System.out.println("a0: " + a0);
      }
    }

    {
      System.out.println("--- a1 ---");
      for (AccountTx1DAO a1 : AccountTx1DAO.select(200, 0)) {
        System.out.println("a1: " + a1);
      }
    }

    {
      System.out.println("--- big account tx ---");
      for (BigAccountTxDAO ba : BigAccountTxDAO.select(200, 0)) {
        System.out.println("ba: " + ba);
      }
    }

  }

  private static void selectByUI() throws SQLException {
    ConfigValuesDAO example = new ConfigValuesDAO();
    example.setName("prop3");
    for (ConfigValuesDAO v : ConfigValuesDAO.selectByExample(example)) {
      System.out.println("v: " + v);
    }

    // AccountDAO a = AccountDAO.select(1234005);
    // for (TransactionDAO tx : a.selectChildrenTransaction().byAccountId()) {
    // System.out.println("tx: " + tx);
    // }

    System.out.println("===");
    for (MultParamSelectDAO mp : MultParamSelectDAO.select(160)) {
      System.out.println("mp=" + mp);
    }

  }

}
