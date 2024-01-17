package app.test.assembly;

import java.util.List;

import app.test.base.Table;

public class Select1<A> {

  private Table<?> t;
  private A a;

  public Select1(Table<?> t, A a) {
    this.t = t;
    this.a = a;
  }

  public <T extends Table<B>, B> Select2<A, B> join(T t) {
    return new Select2<A, B>(t, a, t.getVO());
  }

  public Select1Entities<A> entities() {
    return new Select1Entities<A>(t, a);
  }

  public List<Tuple1<A>> execute() {
    return null;
  }

}
