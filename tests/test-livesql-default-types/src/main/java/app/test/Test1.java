package app.test;

import java.util.List;
import java.util.Map;

import app.test.assembly.SQL;
import app.test.assembly.XTuple1;
import app.test.assembly.XTuple2;
import app.test.assembly.XTuple3;
import app.test.domain.AutoDAO;
import app.test.domain.AutoTable;
import app.test.domain.AutoVO;
import app.test.domain.BusDAO;
import app.test.domain.BusTable;
import app.test.domain.BusVO;
import app.test.domain.CarDAO;
import app.test.domain.CarTable;
import app.test.domain.CarVO;

public class Test1 {

  public static void main(String[] args) {
    m1();
  }

  private static void m1() {
    SQL sql = new SQL();

//    AccountTable a = AccountDAO.newTable("a");

    AutoTable<AutoVO> t = AutoDAO.entity();
    BusTable<BusVO> u = BusDAO.entity();
    CarTable<CarVO> v = CarDAO.entity();

    // Una entidad

    List<XTuple1<AutoVO>> rows = sql.select().from(t).entities().execute();

    for (XTuple1<AutoVO> r : rows) {
      AutoVO auto = r.get(AutoVO.class);
      // UnboundColumns unboundCols = r.getUnboundColumns();
      Map<String, Object> unboundCols = r.getUnboundColumns();
    }

    // Dos entidades

    List<XTuple2<AutoVO, BusVO>> rows2 = sql.select().from(t).join(u).entities().execute();

    for (XTuple2<AutoVO, BusVO> r : rows2) {
      AutoVO auto = r.get(AutoVO.class);
      BusVO bus = r.get(BusVO.class);
      // UnboundColumns unboundCols = r.getUnboundColumns();
      Map<String, Object> unboundCols = r.getUnboundColumns();
    }

    // Tres entidades

    List<XTuple3<AutoVO, BusVO, CarVO>> rows3 = sql.select().from(t).join(u).join(v).entities().execute();

    for (XTuple3<AutoVO, BusVO, CarVO> r : rows3) {
      AutoVO auto = r.get(AutoVO.class);
      BusVO bus = r.get(BusVO.class);
      CarVO car = r.get(CarVO.class);
      // UnboundColumns unboundCols = r.getUnboundColumns();
      Map<String, Object> unboundCols = r.getUnboundColumns();
    }

  }

}
