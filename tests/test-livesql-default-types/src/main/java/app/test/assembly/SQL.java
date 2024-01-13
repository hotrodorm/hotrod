package app.test.assembly;

import app.test.base.Table;

public class SQL {

  public <T extends Table<A>, A> Select1<A> selectFrom(T t) {
    return new Select1<A>(t, t.getVO());
  }

}
