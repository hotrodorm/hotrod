package app.test;

import java.util.ArrayList;
import java.util.List;

public class Select1<A> {

  private A a;

  public Select1(Table<?> t, A a) {
    this.a = a;
  }

  public List<A> execute() {
    List<A> list = new ArrayList<>();
    return list;
  }

  public <B, T extends Table<B>> Select2<A, B> join(T t) {
    return new Select2<A, B>(t, a, t.getVO());
  }

//  public static <T extends Table<V>, V> Select1<V> selectFrom(T t) {
//    return new Select1<V>(t, t.getVO());
//  }

}
