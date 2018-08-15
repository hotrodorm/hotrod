package tests;

import java.io.IOException;
import java.sql.SQLException;

import hotrod.test.generation.Account2VO;
import hotrod.test.generation.AccountVO;
import hotrod.test.generation.Alert2VO;
import hotrod.test.generation.AtelierVO;
import hotrod.test.generation.Car_part_priceVO;
import hotrod.test.generation.HouseVO;
import hotrod.test.generation.primitives.AccountDAO;
import hotrod.test.generation.primitives.AlertFinder;
import hotrod.test.generation.primitives.AtelierDAO;
import hotrod.test.generation.primitives.Car_part_priceDAO;
import hotrod.test.generation.primitives.Executor1;
import hotrod.test.generation.primitives.HouseDAO;

public class SelectTests {

  public static void main(final String[] args) throws IOException, SQLException {
    countProperties();
  }

  private static void countProperties() throws SQLException {

    // selectByExample();

    // selectComplexName();
    // selectOtherSchema();
    // selectMultiSchema();
    // selectCatalogDefaultSchema();
    selectSequenceFromSchema();
  }

  private static void selectByExample() throws SQLException {
    {
      System.out.println("--- a0 ---");
      for (AccountVO a0 : AccountDAO.selectByExample(new AccountVO())) {
        System.out.println("a0: " + a0);
      }
    }
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

  private static void selectCatalogDefaultSchema() throws SQLException {
    System.out.println("=== Atelier ===");
    for (AtelierVO a : AtelierDAO.selectByExample(new AtelierVO())) {
      System.out.println("a: " + a);
    }
    System.out.println("===");
  }

  private static void selectSequenceFromSchema() throws SQLException {
    System.out.println("=== Sequence ===");
    long s1 = Executor1.retrieveMySeq1();
    System.out.println("s1=" + s1);
  }

  private static void selectComplexName() throws SQLException {

    System.out.println("=== Car Part Prices ===");
    for (Car_part_priceVO p : Car_part_priceDAO.selectByExample(new Car_part_priceVO())) {
      System.out.println("p: " + p);
    }
    System.out.println("===");

    // Car_part_priceVO n = new Car_part_priceVO();
    // n.setPart_(1234);
    // n.setPrice_dollar(123456);
    // n.set_discount(15);
    // Car_part_priceDAO.insert(n);

  }

}
