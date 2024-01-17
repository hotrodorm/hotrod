package app.test.assembly;

import java.util.List;

import app.test.base.Table;

public class Select2Entities<A, B> {

  private A a;
  private B b;

  public Select2Entities(Table<?> t, A a, B b) {
    this.a = a;
    this.b = b;
  }

  public List<Tuple2<A, B>> execute() {
    return null;
  }

}
