package app.test;

import java.util.ArrayList;
import java.util.List;

public class Select1<A extends Table<VO>, V> {

  private A a;
  private V v;

  public Select1(A e, V v) {
    this.a = e;
    this.v = v;
  }

  public List<V> execute() {
    List<V> list = new ArrayList<>();
    list.add(this.v);
    return list;
  }

  public <B extends Table<VO>> Select2<A, B> join(B b) {
    return new Select2<>(a, b);
  }

}
