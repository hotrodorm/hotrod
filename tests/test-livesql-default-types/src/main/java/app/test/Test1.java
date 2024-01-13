package app.test;

import java.util.List;

import app.test.list.List2;

public class Test1 {

  public static void main(String[] args) {
    m1();
  }

  private static void m1() {
    SQL sql = new SQL();

    AutoTable<AutoVO> t = AutoDAO.newTable();
    BusTable<BusVO> u = BusDAO.newTable();

    List<AutoVO> rows = sql.selectFrom(t).execute();

    List2<AutoVO, BusVO> rows2 = sql.selectFrom(t).join(u).execute();

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
