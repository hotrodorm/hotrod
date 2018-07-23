package tests;

import java.io.IOException;
import java.sql.SQLException;

import hotrod.test.generation.AccountTx0;
import hotrod.test.generation.AccountTx1;
import hotrod.test.generation.ConfigValuesVO;
import hotrod.test.generation.MultParamSelect;
import hotrod.test.generation.primitives.ConfigValuesDAO;
import hotrod.test.generation.primitives.MyDAO;

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
      for (AccountTx0 a0 : MyDAO.getAccountTx0()) {
        System.out.println("a0: " + a0);
      }
    }

    {
      System.out.println("--- a1 ---");
      for (AccountTx1 a1 : MyDAO.getAccountTx1(200, 0)) {
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
    for (MultParamSelect mp : MyDAO.getMultParamSelect(160)) {
      System.out.println("mp=" + mp);
    }

  }

}
