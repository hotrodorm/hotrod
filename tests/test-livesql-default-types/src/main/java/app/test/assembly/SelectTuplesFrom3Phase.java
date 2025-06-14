package app.test.assembly;

import java.util.List;

import app.test.base.Table;

public class SelectTuplesFrom3Phase<A, B, C> {

  private SelectQuery query;

  public SelectTuplesFrom3Phase(SelectQuery query, Table<?> t) {
    this.query.add(t);
  }

  public SelectWherePhase<XTuple3<A, B, C>> where() {
    return new SelectWherePhase<XTuple3<A, B, C>>(this.query);
  }

  public List<XTuple3<A, B, C>> execute() {
    return null;
  }

}
