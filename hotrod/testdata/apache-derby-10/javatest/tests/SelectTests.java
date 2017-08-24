package tests;

import java.sql.SQLException;

import hotrod.test.generation.AccountVO;
import hotrod.test.generation.primitives.AccountDAO;

public class SelectTests {

  public static void main(final String[] args) throws SQLException {
    showAccounts();
  }

  private static void showAccounts() throws SQLException {
    System.out.println("Show all accounts:");
    System.out.println("==================");
    for (AccountVO a : AccountDAO.selectByExample(new AccountVO())) {
      System.out.println("a=" + a);
    }
  }

}
