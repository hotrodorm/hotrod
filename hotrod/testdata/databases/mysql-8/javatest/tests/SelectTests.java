package tests;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;

import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.SqlSession;
import org.hotrod.runtime.interfaces.DaoWithOrder;
import org.hotrod.runtime.tx.TxManager;

import hotrod.test.generation.Account2VO;
import hotrod.test.generation.AccountTx0VO;
import hotrod.test.generation.AccountTx1VO;
import hotrod.test.generation.Alert2VO;
import hotrod.test.generation.BigAccountTxVO;
import hotrod.test.generation.Car_part_priceVO;
import hotrod.test.generation.ConfigValuesVO;
import hotrod.test.generation.CursorExample1VO;
import hotrod.test.generation.HouseVO;
import hotrod.test.generation.MultParamSelectVO;
import hotrod.test.generation.VehicleVO;
import hotrod.test.generation.primitives.AlertFinder;
import hotrod.test.generation.primitives.Car_part_priceDAO;
import hotrod.test.generation.primitives.ConfigValuesDAO;
import hotrod.test.generation.primitives.CursorExample1DAO;
import hotrod.test.generation.primitives.CursorExample1DAO.CursorExample1OrderBy;
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

//    selectByEnumUI();

    selectByCursor();
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

  // Select using cursor

  private static void selectByCursor() throws SQLException {

    boolean useCursor = true;

    CursorExample1VO example = new CursorExample1VO();
    long rows = 0;
    long sum = 0;
    showHeapUsage("BEFORE");

    if (useCursor) {
      showHeapUsage("CUR-00");

      TxManager txm = CursorExample1DAO.getTxManager();
      SqlSession sqlSession = txm.getSqlSession();

      DaoWithOrder<CursorExample1VO, CursorExample1OrderBy> dwo = //
          new DaoWithOrder<CursorExample1VO, CursorExample1OrderBy>(example, new CursorExample1OrderBy[0]);
      Cursor<CursorExample1VO> dr = sqlSession
          .selectCursor("hotrod.test.generation.primitives.cursorExample1.selectByExample", dwo);
      showHeapUsage("CUR-01");

      for (CursorExample1VO tb : dr) {
        sum = sum + tb.getId();
        rows++;
      }

      System.gc();

      showHeapUsage("CUR-02");
      System.out.println("Total rows=" + rows + " sum=" + sum);

    } else {
      List<CursorExample1VO> dr = CursorExample1DAO.selectByExample(example);
      showHeapUsage("AFTER1");
      for (CursorExample1VO tb : dr) {
        sum = sum + tb.getId();
        rows++;
      }
      System.out.println("Total rows=" + rows + " sum=" + sum);
    }
  }

  private static void showHeapUsage(final String title) {
    Runtime r = Runtime.getRuntime();
    long totalK = r.totalMemory() / 1014;
    long freeK = r.freeMemory() / 1024;
    long usedK = totalK - freeK;
    DecimalFormat pf = new DecimalFormat("0.00%");
    System.out.println(title + ": " + pf.format(1.0 * usedK / totalK) + " = " + usedK + " kB / " + totalK + " kB");
  }

}
