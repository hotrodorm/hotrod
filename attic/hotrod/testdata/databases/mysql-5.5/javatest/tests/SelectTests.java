package tests;

import java.io.IOException;
import java.sql.SQLException;

import hotrod.test.generation.Account2VO;
import hotrod.test.generation.AccountTx0VO;
import hotrod.test.generation.AccountTx1VO;
import hotrod.test.generation.Alert2VO;
import hotrod.test.generation.BigAccountTxVO;
import hotrod.test.generation.Car_part_priceVO;
import hotrod.test.generation.ConfigValuesVO;
import hotrod.test.generation.HouseVO;
import hotrod.test.generation.MultParamSelectVO;
import hotrod.test.generation.VehicleVO;
import hotrod.test.generation.primitives.AlertFinder;
import hotrod.test.generation.primitives.Car_part_priceDAO;
import hotrod.test.generation.primitives.ConfigValuesDAO;
import hotrod.test.generation.primitives.Executor1;
import hotrod.test.generation.primitives.HouseDAO;
import hotrod.test.generation.primitives.VehicleDAO;
import hotrod.test.generation.primitives.VehicleType;
import hotrod.test.generation.primitives.VehicleVin;

public class SelectTests {

  public static void main(final String[] args) throws IOException, SQLException {
    // selectByExample();
    // selectByUI();

    // selectOtherCatalog();
    // selectMultiCatalog();
    // selectComplexName();

    selectByEnumUI();
  }

  private static void selectByExample() throws SQLException {

    {
      System.out.println("--- a0 ---");
      for (AccountTx0VO a0 : Executor1.findAccountTx0()) {
        System.out.println("a0: " + a0);
      }
    }

    {
      System.out.println("--- a1 ---");
      for (AccountTx1VO a1 : Executor1.findAccountTx1(200, 0)) {
        System.out.println("a1: " + a1);
      }
    }

    {
      System.out.println("--- big account tx ---");
      for (BigAccountTxVO ba : Executor1.findBigAccountTx(200, 0)) {
        System.out.println("ba: " + ba);
      }
    }

  }

  private static void selectByUI() throws SQLException {
    ConfigValuesVO example = new ConfigValuesVO();
    example.setName("prop3");
    for (ConfigValuesVO v : ConfigValuesDAO.selectByExample(example)) {
      System.out.println("v: " + v);
    }

    // AccountDAO a = AccountDAO.select(1234005);
    // for (TransactionDAO tx : a.selectChildrenTransaction().byAccountId()) {
    // System.out.println("tx: " + tx);
    // }

    System.out.println("===");
    for (MultParamSelectVO mp : Executor1.findMultParamSelect(160)) {
      System.out.println("mp=" + mp);
    }

  }

  private static void selectByEnumUI() throws SQLException {
    System.out.println("=== select by Enum UI ===");

    VehicleVO v1 = VehicleDAO.selectByUIVin(VehicleVin._2018_02_07);
    System.out.println("v1=" + v1);

    VehicleVO v2 = VehicleDAO.selectByUIVtype(VehicleType.SEDAN);
    System.out.println("v2=" + v2);

    System.out.println("===");

  }

  private static void selectOtherCatalog() throws SQLException {
    System.out.println("=== House ===");
    for (HouseVO h : HouseDAO.selectByExample(new HouseVO())) {
      System.out.println("h: " + h);
    }
    System.out.println("===");
  }

  private static void selectMultiCatalog() throws SQLException {
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

    // Car_part_priceVO n = new Car_part_priceVO();
    // n.setPart_(1234);
    // n.setPrice_dollar(123456);
    // n.set_discount(15);
    // Car_part_priceDAO.insert(n);

  }

}
