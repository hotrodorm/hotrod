package app.test.assembly;

import java.util.List;

import app.test.base.Table;

public class SelectTuplesFrom2Phase<A, B> {

  private SelectQuery query;

  public SelectTuplesFrom2Phase(SelectQuery query, Table<B> t) {
    this.query.add(t);
  }

  public <T extends Table<C>, C> SelectTuplesFrom3Phase<A, B, C> join(T t) {
    return new SelectTuplesFrom3Phase<A, B, C>(this.query, t);
  }

  public SelectWherePhase<XTuple2<A, B>> where() {
    return new SelectWherePhase<XTuple2<A, B>>(this.query);
  }

  public List<XTuple2<A, B>> execute() {
    return null;
  }

}
