package tests;

import java.sql.SQLException;

import hotrod.test.generation.Account2VO;
import hotrod.test.generation.AccountTx2DAO;
import hotrod.test.generation.Alert2VO;
import hotrod.test.generation.Car_part_priceVO;
import hotrod.test.generation.HouseVO;
import hotrod.test.generation.SpecialColumnsVO;
import hotrod.test.generation.primitives.AlertFinder;
import hotrod.test.generation.primitives.Car_part_priceDAO;
import hotrod.test.generation.primitives.Executor1;
import hotrod.test.generation.primitives.HouseDAO;
import hotrod.test.generation.primitives.SpecialColumnsDAO;

public class SelectTests {

  public static void main(final String[] args) throws SQLException {
    // testColumns();
    // testDynamicSelect();

    // selectOtherSchema();
    // selectMultiSchema();
    selectComplexName();

  }

  private static void testDynamicSelect() throws SQLException {
    showDynamicSelectResult(140);
    showDynamicSelectResult(null);
  }

  private static void showDynamicSelectResult(final Integer maxAmount) throws SQLException {
    System.out.println("--- Dynamic Select with maxamount=" + maxAmount + " ---");
    for (AccountTx2DAO at : Executor1.findAccountTx2DAO(maxAmount)) {
      System.out.println("[maxamount=" + maxAmount + "] at=" + at);
    }
    System.out.println("---");
  }

  private static void selectOtherSchema() throws SQLException {
    System.out.println("=== House ===");
    for (HouseVO h : HouseDAO.selectByExample(new HouseVO())) {
      System.out.println("h: " + h);
    }
    System.out.println("===");
  }

  private static void selectMultiSchema() throws SQLException {
    System.out.println("=== Tree ===");
    for (Account2VO acc : AlertFinder.findAlerts()) {
      System.out.println("account: " + acc);
      for (Alert2VO a : acc.getAlerts()) {
        System.out.println(" + alert: " + a);
        System.out.println(" + house: " + a.getHouse());
      }
    }
    System.out.println("===");
  }

  private static void selectComplexName() throws SQLException {

    System.out.println("=== Car Part Prices ===");
    for (Car_part_priceVO p : Car_part_priceDAO.selectByExample(new Car_part_priceVO())) {
      System.out.println("p: " + p);
    }
    System.out.println("===");

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
