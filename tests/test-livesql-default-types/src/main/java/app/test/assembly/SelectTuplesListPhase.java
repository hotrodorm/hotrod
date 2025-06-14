package app.test.assembly;

import app.test.base.Table;

public class SelectTuplesListPhase {

  public <T extends Table<A>, A> SelectTuplesFrom1Phase<A> from(T t) {
    return new SelectTuplesFrom1Phase<A>(t);
  }

}
