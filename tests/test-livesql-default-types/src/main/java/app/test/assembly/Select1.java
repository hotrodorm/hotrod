package app.test.assembly;

import java.util.ArrayList;
import java.util.List;

import app.test.base.Table;

public class Select1<A> {

  private A a;

  public Select1(Table<?> t, A a) {
    this.a = a;
  }

  public <T extends Table<B>, B> Select2<A, B> join(T t) {
    return new Select2<A, B>(t, a, t.getVO());
  }

  public List<A> execute() {
    List<A> list = new ArrayList<>();
    return list;
  }

}
