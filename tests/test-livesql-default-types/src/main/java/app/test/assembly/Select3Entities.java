package app.test.assembly;

import java.util.List;

import app.test.base.Table;

public class Select3Entities<A, B, C> {

  private A a;
  private B b;
  private C c;

  public Select3Entities(Table<?> t, A a, B b, C c) {
    this.a = a;
    this.b = b;
    this.c = c;
  }

  public List<Tuple3<A, B, C>> execute() {
    return null;
  }

}
