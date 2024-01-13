package app.test;

import java.util.List;

import app.test.assembly.List2;
import app.test.assembly.List2.Tuple2;
import app.test.assembly.List3;
import app.test.assembly.List3.Tuple3;
import app.test.assembly.SQL;
import app.test.assembly.Select1;
import app.test.base.Table;
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

    List<AutoVO> rows = sql.selectFrom(t).execute();

    for (AutoVO a : rows) {

    }

    List2<AutoVO, BusVO> rows2 = sql.selectFrom(t).join(u).execute();

    for (Tuple2<AutoVO, BusVO> r : rows2) {
      AutoVO auto = r.get1();
      BusVO bus = r.get2();
    }

    List3<AutoVO, BusVO, CarVO> rows3 = sql.selectFrom(t).join(u).join(v).execute();

    for (Tuple3<AutoVO, BusVO, CarVO> r : rows3) {
      AutoVO auto = r.get1();
      BusVO bus = r.get2();
      CarVO car = r.get3();
    }

  }

//  private static <T extends Table<VO>, V extends VO> Select1<T, V> select(T a, V v) {
//    return new Select1<T, V>(a, v);
//  }

//  private static <T extends Table<VO>, V extends VO> Select1<T, V> select(T a, V v) {
//    return new Select1<T, V>(a,(V) a.getVO());
//  }

//  private static <V extends VO, T extends Table<V>> Select1<V, T> select(T t) {
//    return new Select1<V, T>(t, t.getVO());
//  }

  private static <T extends Table<V>, V> Select1<V> from(T t) {
    V v = t.getVO();
    return new Select1<V>(t, v);
//    return null;
  }

//  private static <T extends Table<V>, V extends VO> Select1<V> from(T t) {
//    V v = t.getVO();
//    Picker<V> p = new Picker<V>(t);
//    V v2 = p.first();
//    
//    return p.first();
////    return null;
//  }

}
