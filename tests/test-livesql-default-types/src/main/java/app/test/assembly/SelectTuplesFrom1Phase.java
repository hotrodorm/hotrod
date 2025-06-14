package app.test.assembly;

import java.util.List;

import app.test.base.Table;

public class SelectTuplesFrom1Phase<A> {

  private SelectQuery query;

  public SelectTuplesFrom1Phase(Table<?> t) {
    this.query.add(t);
  }

  public <T extends Table<B>, B> SelectTuplesFrom2Phase<A, B> join(T t) {
    return new SelectTuplesFrom2Phase<A, B>(this.query, t);
  }

  public SelectWherePhase<XTuple1<A>> where() {
    return new SelectWherePhase<XTuple1<A>>(this.query);
  }

  public List<XTuple1<A>> execute() {
    return null;
  }

}
