package app.test;

import app.test.list.List2;

public class Select2<A extends Table, B extends Table> {

  private A a;
  private B b;

  public Select2(A a, B b) {
    this.a = a;
    this.b = b;
  }

  public List2<A, B> execute() {
    List2<A, B> list = new List2<>();
    list.add(this.a, this.b);
    return list;
  }

}
