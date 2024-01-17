package app.test.assembly;

import java.util.List;

import app.test.base.Table;

public class Select2<A, B> {

  private Table<?> t;
  private A a;
  private B b;

  public Select2(Table<?> t, A a, B b) {
    this.t = t;
    this.a = a;
    this.b = b;
  }

  public <T extends Table<C>, C> Select3<A, B, C> join(T t) {
    return new Select3<A, B, C>(t, a, b, t.getVO());
  }

  public Select2Entities<A, B> entities() {
    return new Select2Entities<A, B>(t, a, b);
  }

  public List<Tuple2<A, B>> execute() {
    return null;
  }

}
