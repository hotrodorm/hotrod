package tests;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.SqlSession;
import org.hotrod.runtime.interfaces.DaoWithOrder;
import org.hotrod.runtime.tx.TxManager;

import hotrod.test.generation.Account2VO;
import hotrod.test.generation.AccountVO;
import hotrod.test.generation.Alert2VO;
import hotrod.test.generation.ConfigValuesVO;
import hotrod.test.generation.CursorExample1VO;
import hotrod.test.generation.HouseVO;
import hotrod.test.generation.TxBranchVO;
import hotrod.test.generation._price_VO;
import hotrod.test.generation.primitives.AccountDAO;
import hotrod.test.generation.primitives.AccountDAO.AccountOrderBy;
import hotrod.test.generation.primitives.AlertFinder;
import hotrod.test.generation.primitives.ConfigValuesDAO;
import hotrod.test.generation.primitives.CursorExample1DAO;
import hotrod.test.generation.primitives.CursorExample1DAO.CursorExample1OrderBy;
import hotrod.test.generation.primitives.HouseDAO;
import hotrod.test.generation.primitives.TxBranchDAO;
import hotrod.test.generation.primitives._price_DAO;

public class SelectTests {

  public static void main(final String[] args) throws IOException, SQLException {
    countProperties();
  }

  private static void countProperties() throws SQLException {
    // selectByExample();
    // selectByUI();
    // selectSortedByName();
    // selectByExampleWithNull();
    // selectTag();
    // tryInsertBadData();
    // selectViewSequenceAndQuery();

    // selectOtherSchema();
    // selectMultiSchema();
    // selectComplexName();

    selectByCursor();
  }

  private static void tryInsertBadData() throws SQLException {
    AccountVO a = new AccountVO();
    AccountDAO.insert(a);
  }

  private static void selectByExample() throws SQLException {
    TxBranchVO example = new TxBranchVO();
    example.setBranchId(103);
    for (TxBranchVO tb : TxBranchDAO.selectByExample(example)) {
      System.out.println("tb=" + tb);
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

  private static void selectByUI() throws SQLException {
    ConfigValuesVO example = new ConfigValuesVO();
    example.setName("prop3");
    for (ConfigValuesVO v : ConfigValuesDAO.selectByExample(example)) {
      System.out.println("v: " + v);
    }

  }

  private static void selectByExampleWithNull() throws SQLException {
    AccountVO example = new AccountVO();
    example.setType("CHK");
    example.setCurrentBalance(null);

    for (AccountVO a : AccountDAO.selectByExample(example)) {
      System.out.println(a);
    }

  }

  private static void selectSortedByName() throws SQLException {
    AccountVO example = new AccountVO();

    System.out.println();
    System.out.println("--- All accounts (sorted by name) ---");
    for (AccountVO a : AccountDAO.selectByExample(example, AccountOrderBy.NAME)) {
      System.out.println("1: " + a);
    }

    System.out.println();
    System.out.println("--- All accounts (sorted by name, case insensitive) ---");
    for (AccountVO a : AccountDAO.selectByExample(example, AccountOrderBy.NAME$CASEINSENSITIVE)) {
      System.out.println("2: " + a);
    }

    System.out.println();
    System.out.println("--- All accounts (sorted by name, case insensitive, stable forward) ---");
    for (AccountVO a : AccountDAO.selectByExample(example, AccountOrderBy.NAME$CASEINSENSITIVE_STABLE_FORWARD)) {
      System.out.println("3: " + a);
    }

    System.out.println();
    System.out.println("--- All accounts (sorted by name, case insensitive, stable reverse) ---");
    for (AccountVO a : AccountDAO.selectByExample(example, AccountOrderBy.NAME$CASEINSENSITIVE_STABLE_REVERSE)) {
      System.out.println("4: " + a);
    }

  }

  private static void selectComplexName() throws SQLException {
    System.out.println("=== Complex Name ===");
    for (_price_VO p : _price_DAO.selectByExample(new _price_VO())) {
      System.out.println("p: " + p);
    }
    System.out.println("===");
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

  private static void selectViewSequenceAndQuery() throws SQLException {
    System.out.println("TxBranch:");
    System.out.println("=========");

    long codeSeq = TxBranchDAO.getCodeSequence();
    System.out.println("codeSeq=" + codeSeq);

    Date from = new java.text.SimpleDateFormat("yyyyMMdd-HHmmss").parse("20160101-000000",
        new java.text.ParsePosition(0));
    Date to = new java.text.SimpleDateFormat("yyyyMMdd-HHmmss").parse("20170101-000000",
        new java.text.ParsePosition(0));

    int rows = TxBranchDAO.applyAccountPromotion75(10, from, to, -1);
    System.out.println("promotios rows=" + rows);

  }

}
