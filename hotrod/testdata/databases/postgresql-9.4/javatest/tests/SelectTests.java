package tests;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.hotrod.runtime.interfaces.DaoWithOrder;
import org.hotrod.runtime.tx.TxManager;

import hotrod.test.generation.Account2VO;
import hotrod.test.generation.AccountTx0;
import hotrod.test.generation.AccountTx1;
import hotrod.test.generation.Alert2VO;
import hotrod.test.generation.Car_part_priceVO;
import hotrod.test.generation.ConfigValuesVO;
import hotrod.test.generation.HouseVO;
import hotrod.test.generation.MultParamSelect;
import hotrod.test.generation.TypesDateTimeVO;
import hotrod.test.generation.primitives.AlertFinder;
import hotrod.test.generation.primitives.Car_part_priceDAO;
import hotrod.test.generation.primitives.ConfigValuesDAO;
import hotrod.test.generation.primitives.HouseDAO;
import hotrod.test.generation.primitives.HouseDAO.HouseOrderBy;
import hotrod.test.generation.primitives.MyDAO;
import hotrod.test.generation.primitives.TypesDateTimeDAO;

public class SelectTests {

  public static void main(final String[] args) throws IOException, SQLException {
    countProperties();
  }

  private static void countProperties() throws SQLException {
    // selectByExample();
    // selectByUI();
    // selectComplexName();
    // selectOtherSchema();
    // selectMultiSchema();
    // selectTimestampWithoutTimeZone();
    selectCursor();
  }

  private static void selectByExample() throws SQLException {

    {
      System.out.println("--- a0 ---");
      for (AccountTx0 a0 : MyDAO.getAccountTx0()) {
        System.out.println("a0: " + a0);
      }
    }

    {
      System.out.println("--- a1 ---");
      for (AccountTx1 a1 : MyDAO.getAccountTx1(200, 0)) {
        System.out.println("a1: " + a1);
      }
    }

  }

  private static void selectTimestampWithoutTimeZone() throws SQLException {
    System.out.println("=== TS without time zone ===");
    for (TypesDateTimeVO ts : TypesDateTimeDAO.selectByExample(new TypesDateTimeVO())) {
      System.out.println("ts: " + ts);
      System.out.println("  -> ts2=" + ts.getTs2());
      Date d2 = new Date(ts.getTs2().getTime());
      System.out.println("  -> dt2=" + d2);
    }

    // TypesDateTimeVO t2 = new TypesDateTimeVO();
    // t2.setId(1234);
    // t2.setTs2(new java.util.Date());
    // TypesDateTimeDAO.insert(t2);

    System.out.println("===");
  }

  private static void selectCursor() throws SQLException {
    System.out.println("=== House using Cursor ===");

    SqlSessionFactory f = sessionfactory.DatabaseSessionFactory.getInstance().getSqlSessionFactory();
    TxManager txm = new TxManager(f);

    SqlSession sqlSession = txm.getSqlSession();

    DaoWithOrder<HouseVO, HouseOrderBy> dwo = new DaoWithOrder<HouseVO, HouseOrderBy>(new HouseVO(),
        new HouseOrderBy[0]);

    // List<HouseVO> houses =
    // sqlSession.selectList("hotrod.test.generation.primitives.house.selectByExample",
    // dwo);

    Cursor<HouseVO> houses = sqlSession.selectCursor("hotrod.test.generation.primitives.house.selectByExample", dwo);
    
    for (HouseVO h : houses) {
      System.out.println("h: " + h);
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

  private static void selectByUI() throws SQLException {
    ConfigValuesVO example = new ConfigValuesVO();
    example.setName("prop3");
    for (ConfigValuesVO v : ConfigValuesDAO.selectByExample(example)) {
      System.out.println("v: " + v);
    }

    System.out.println("===");
    for (MultParamSelect mp : MyDAO.getMultParamSelect(160)) {
      System.out.println("mp=" + mp);
    }

  }

}
