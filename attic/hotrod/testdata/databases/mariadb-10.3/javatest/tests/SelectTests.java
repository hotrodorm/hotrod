package tests;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;

import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.SqlSession;
import org.hotrod.runtime.interfaces.DaoWithOrder;
import org.hotrod.runtime.tx.TxManager;

import hotrod.test.generation.ConfigValuesVO;
import hotrod.test.generation.CursorExample1VO;
import hotrod.test.generation.primitives.ConfigValuesDAO;
import hotrod.test.generation.primitives.CursorExample1DAO;
import hotrod.test.generation.primitives.CursorExample1DAO.CursorExample1OrderBy;

public class SelectTests {

  public static void main(final String[] args) throws IOException, SQLException {
    countProperties();
  }

  private static void countProperties() throws SQLException {

//    selectByExample();
    // selectByUI();
    selectByCursor() ;
  }

//  private static void selectByExample() throws SQLException {
//
//    {
//      System.out.println("--- a0 ---");
//      for (AccountTx0VO a0 : AccountTx0DAO.select()) {
//        System.out.println("a0: " + a0);
//      }
//    }
//
//    {
//      System.out.println("--- a1 ---");
//      for (AccountTx1VO a1 : AccountTx1DAO.select(200, 0)) {
//        System.out.println("a1: " + a1);
//      }
//    }
//
//  }
//
//  private static void selectByUI() throws SQLException {
//    ConfigValuesVO example = new ConfigValuesVO();
//    example.setName("prop3");
//    for (ConfigValuesVO v : ConfigValuesDAO.selectByExample(example)) {
//      System.out.println("v: " + v);
//    }
//
//    System.out.println("===");
//    for (MultParamSelectVO mp : MultParamSelectDAO.select(160)) {
//      System.out.println("mp=" + mp);
//    }
//
//  }


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
