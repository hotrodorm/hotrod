package app.test.assembly;

import java.util.List;

import app.test.base.Table;

public class Select3<A, B, C> {

  private Table<?> t;
  private A a;
  private B b;
  private C c;

  public Select3(Table<?> t, A a, B b, C c) {
    this.t = t;
    this.a = a;
    this.b = b;
    this.c = c;
  }

  public Select3Entities<A, B, C> entities() {
    return new Select3Entities<A, B, C>(t, a, b, c);
  }

  public List<Tuple3<A, B, C>> execute() {
    return null;
  }

}
