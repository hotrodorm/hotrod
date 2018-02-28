package tests;

import java.sql.SQLException;

import hotrod.test.generation.AccountTx2VO;
import hotrod.test.generation.SpecialColumnsVO;
import hotrod.test.generation.primitives.AccountTx2DAO;
import hotrod.test.generation.primitives.SpecialColumnsDAO;

public class SelectTests {

  public static void main(final String[] args) throws SQLException {
    testColumns();
    // testDynamicSelect();
  }

  private static void testDynamicSelect() throws SQLException {
    showDynamicSelectResult(140);
    showDynamicSelectResult(null);
  }

  private static void showDynamicSelectResult(final Integer maxAmount) throws SQLException {
    System.out.println("--- Dynamic Select with maxamount=" + maxAmount + " ---");
    for (AccountTx2VO at : AccountTx2DAO.select(maxAmount)) {
      System.out.println("[maxamount=" + maxAmount + "] at=" + at);
    }
    System.out.println("---");
  }

  private static void testColumns() throws SQLException {

    // delete all rows
    int rows = SpecialColumnsDAO.deleteByExample(new SpecialColumnsVO());
    System.out.println(rows + " row(s) deleted.");

    {
      SpecialColumnsVO s1 = new SpecialColumnsVO();
      s1.setColumn("c");
      s1.setCreatedAt("d");
      s1.setFrom("e");
      s1.setId(1);
      s1.setTable("f");
      s1.setValueId("g1");
      SpecialColumnsDAO.insert(s1);
    }

    {
      SpecialColumnsVO s1 = new SpecialColumnsVO();
      s1.setColumn("c2");
      s1.setCreatedAt("d2");
      s1.setFrom("e2");
      s1.setId(2);
      s1.setTable("f2");
      s1.setValueId("g2");
      SpecialColumnsDAO.insert(s1);
    }

    SpecialColumnsVO example = new SpecialColumnsVO();
    // filter.setValueId("g2");
    for (SpecialColumnsVO s : SpecialColumnsDAO.selectByExample(example)) {
      System.out.println("s=" + s);
    }

  }

}
