package app.test;

import java.util.List;
import java.util.Map;

import app.test.assembly.SQL;
import app.test.assembly.Tuple1;
import app.test.assembly.Tuple2;
import app.test.assembly.Tuple3;
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

    AutoTable<AutoVO> t = AutoDAO.newTable();
    BusTable<BusVO> u = BusDAO.newTable();
    CarTable<CarVO> v = CarDAO.newTable();

    // Una entidad

    List<Tuple1<AutoVO>> rows = sql.select().from(t).entities().execute();

    for (Tuple1<AutoVO> r : rows) {
      AutoVO auto = r.get(AutoVO.class);
      // UnboundColumns unboundCols = r.getUnboundColumns();
      Map<String, Object> unboundCols = r.getUnboundColumns();
    }

    // Dos entidades

    List<Tuple2<AutoVO, BusVO>> rows2 = sql.select().from(t).join(u).entities().execute();

    for (Tuple2<AutoVO, BusVO> r : rows2) {
      AutoVO auto = r.get(AutoVO.class);
      BusVO bus = r.get(BusVO.class);
      // UnboundColumns unboundCols = r.getUnboundColumns();
      Map<String, Object> unboundCols = r.getUnboundColumns();
    }

    // Tres entidades

    List<Tuple3<AutoVO, BusVO, CarVO>> rows3 = sql.select().from(t).join(u).join(v).entities().execute();

    for (Tuple3<AutoVO, BusVO, CarVO> r : rows3) {
      AutoVO auto = r.get(AutoVO.class);
      BusVO bus = r.get(BusVO.class);
      CarVO car = r.get(CarVO.class);
      // UnboundColumns unboundCols = r.getUnboundColumns();
      Map<String, Object> unboundCols = r.getUnboundColumns();
    }

  }

}
