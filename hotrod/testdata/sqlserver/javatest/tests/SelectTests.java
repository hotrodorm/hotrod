package tests;

import java.io.IOException;
import java.sql.SQLException;

import hotrod.test.generation.AccountDAO;

public class SelectTests {

  public static void main(final String[] args) throws IOException, SQLException {
    countProperties();
  }

  private static void countProperties() throws SQLException {

    selectByExample();

  }

  private static void selectByExample() throws SQLException {

    {
      System.out.println("--- a0 ---");
      for (AccountDAO a0 : AccountDAO.selectByExample(new AccountDAO())) {
        System.out.println("a0: " + a0);
      }
    }

  }

}
