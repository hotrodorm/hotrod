package tests;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;

import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.SqlSession;
import org.hotrod.runtime.interfaces.DaoWithOrder;
import org.hotrod.runtime.tx.TxManager;

import hotrod.test.generation.Account2VO;
import hotrod.test.generation.AccountTx2DAO;
import hotrod.test.generation.Alert2VO;
import hotrod.test.generation.Car_part_priceVO;
import hotrod.test.generation.CursorExample1VO;
import hotrod.test.generation.HouseVO;
import hotrod.test.generation.SpecialColumnsVO;
import hotrod.test.generation.primitives.AlertFinder;
import hotrod.test.generation.primitives.Car_part_priceDAO;
import hotrod.test.generation.primitives.CursorExample1DAO;
import hotrod.test.generation.primitives.Executor1;
import hotrod.test.generation.primitives.HouseDAO;
import hotrod.test.generation.primitives.SpecialColumnsDAO;
import hotrod.test.generation.primitives.CursorExample1DAO.CursorExample1OrderBy;

public class SelectTests {

  public static void main(final String[] args) throws SQLException {
    // testColumns();
    // testDynamicSelect();

    // selectOtherSchema();
    // selectMultiSchema();
    // selectComplexName();

    selectByCursor();

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
