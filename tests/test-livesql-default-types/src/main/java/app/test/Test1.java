package app.test;

import java.util.List;

public class Test1 {

  public static void main(String[] args) {
    m1();
  }

  private static void m1() {
    SQL sql = new SQL();
    AutoTable<Auto> a = AutoDAO.newTable();
    BusTable<Bus> b = BusDAO.newTable();

    Select1<AutoTable<Auto>, Auto> s = new Select1<>(a, new Auto());
    List<Auto> l1 = s.execute();

    Select2<AutoTable<Auto>, BusTable<Bus>> s2 = s.join(b);
//    List2<Auto, Bus> rows = 
    s2.execute();
  }

  private static void m2() {
    Auto a = new Auto();
    Bus b = new Bus();

//    List2<Auto, Bus> rows = new Select1<>(a, new Auto()).join(b).execute();
  }

//  private Select1<T> select(Table<VO> t, VO v) {
//
//  }

}
