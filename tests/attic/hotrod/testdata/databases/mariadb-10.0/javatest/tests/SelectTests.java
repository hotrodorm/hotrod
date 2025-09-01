package tests;

import java.io.IOException;
import java.sql.SQLException;

import hotrod.test.generation.AccountTx0VO;
import hotrod.test.generation.AccountTx1VO;
import hotrod.test.generation.ConfigValuesVO;
import hotrod.test.generation.MultParamSelectVO;
import hotrod.test.generation.primitives.AccountTx0DAO;
import hotrod.test.generation.primitives.AccountTx1DAO;
import hotrod.test.generation.primitives.ConfigValuesDAO;
import hotrod.test.generation.primitives.MultParamSelectDAO;

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
      for (AccountTx0VO a0 : AccountTx0DAO.select()) {
        System.out.println("a0: " + a0);
      }
    }

    {
      System.out.println("--- a1 ---");
      for (AccountTx1VO a1 : AccountTx1DAO.select(200, 0)) {
        System.out.println("a1: " + a1);
      }
    }

  }

  private static void selectByUI() throws SQLException {
    ConfigValuesVO example = new ConfigValuesVO();
    example.setName("prop3");
    for (ConfigValuesVO v : ConfigValuesDAO.selectByExample(example)) {
      System.out.println("v: " + v);
    }

    System.out.println("===");
    for (MultParamSelectVO mp : MultParamSelectDAO.select(160)) {
      System.out.println("mp=" + mp);
    }

  }

}
