package tests;

import java.io.IOException;
import java.sql.SQLException;

import hotrod.test.generation.AccountTxVO;
import hotrod.test.generation.AccountVO;
import hotrod.test.generation.primitives.AccountDAO;

public class SelectTests {

  public static void main(final String[] args) throws IOException, SQLException {
    // selectAcountsWithTXs();
    selectByExample();
  }

  private static void selectAcountsWithTXs() throws SQLException {
    System.out.println("=== Accounts with TXs ===");
    for (AccountTxVO a : AccountDAO.selectAccountWithTxs()) {
      System.out.println("a: " + a);
    }
  }

  private static void selectByExample() throws SQLException {
    System.out.println("=== Show all accounts ===");
    for (AccountVO a : AccountDAO.selectByExample(new AccountVO())) {
      System.out.println("a: " + a);
    }
  }

  private static void selectByUI() throws SQLException {
    // ConfigValuesDAO example = new ConfigValuesDAO();
    // example.setName("prop3");
    // for (ConfigValuesDAO v : ConfigValuesDAO.selectByExample(example)) {
    // System.out.println("v: " + v);
    // }

    // System.out.println("===");
    // for (MultParamSelectDAO mp : MultParamSelectDAO.select(100)) {
    // System.out.println("mp=" + mp);
    // }
  }

}
