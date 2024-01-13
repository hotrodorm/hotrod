package app.test.assembly;

import app.test.base.Table;

public class Select3<A, B, C> {

  private A a;
  private B b;
  private C c;

  public Select3(Table<?> t, A a, B b, C c) {
    this.a = a;
    this.b = b;
    this.c = c;
  }

  public List3<A, B, C> execute() {
    List3<A, B, C> list = new List3<>();
    return list;
  }

}
