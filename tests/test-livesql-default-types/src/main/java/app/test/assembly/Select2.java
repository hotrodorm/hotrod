package app.test.assembly;

import app.test.base.Table;

public class Select2<A, B> {

  private A a;
  private B b;

  public Select2(Table<?> t, A a, B b) {
    this.a = a;
    this.b = b;
  }

  public <T extends Table<C>, C> Select3<A, B, C> join(T t) {
    return new Select3<A, B, C>(t, a, b, t.getVO());
  }

  public List2<A, B> execute() {
    List2<A, B> list = new List2<>();
    return list;
  }

}
