package app.test.assembly;

import java.util.List;

import app.test.base.Table;

public class Select1Entities<A> {

  private A a;

  public Select1Entities(Table<?> t, A a) {
    this.a = a;
  }

  public List<Tuple1<A>> execute() {
    return null;
  }

}
