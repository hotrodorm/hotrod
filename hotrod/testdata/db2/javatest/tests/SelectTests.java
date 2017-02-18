package tests;

import java.sql.SQLException;

import hotrod.test.generation.AccountTx2DAO;
import hotrod.test.generation.SpecialColumnsDAO;

public class SelectTests {

  public static void main(final String[] args) throws SQLException {
    // testColumns();
    testDynamicSelect();

  }

  private static void testDynamicSelect() throws SQLException {
    showDynamicSelectResult(140);
    showDynamicSelectResult(null);
  }

  private static void showDynamicSelectResult(final Integer maxAmount) throws SQLException {
    System.out.println("--- Dynamic Select with maxamount=" + maxAmount + " ---");
    for (AccountTx2DAO at : AccountTx2DAO.select(maxAmount)) {
      System.out.println("[maxamount=" + maxAmount + "] at=" + at);
    }
    System.out.println("---");
  }

  private static void testColumns() throws SQLException {

    // delete all rows
    int rows = SpecialColumnsDAO.deleteByExample(new SpecialColumnsDAO());
    System.out.println(rows + " row(s) deleted.");

    {
      SpecialColumnsDAO s1 = new SpecialColumnsDAO();
      s1.setColumn("c");
      s1.setCreatedAt("d");
      s1.setFrom("e");
      s1.setId(1);
      s1.setTable("f");
      s1.setValueId("g1");
      s1.insert();
    }

    {
      SpecialColumnsDAO s1 = new SpecialColumnsDAO();
      s1.setColumn("c2");
      s1.setCreatedAt("d2");
      s1.setFrom("e2");
      s1.setId(2);
      s1.setTable("f2");
      s1.setValueId("g2");
      s1.insert();
    }

    SpecialColumnsDAO example = new SpecialColumnsDAO();
    // filter.setValueId("g2");
    for (SpecialColumnsDAO s : SpecialColumnsDAO.selectByExample(example)) {
      System.out.println("s=" + s);
    }

  }

}
