package org.hotrod.runtime.livesql.queries.select.sets;

import org.hotrod.runtime.livesql.expressions.ResultSetColumn;

public class CombinedSelectLinkingPhase<R> {

  private SetOperator<R> op;

  public CombinedSelectLinkingPhase(final SetOperator<R> op) {
    this.op = op;
  }

  // Select

  public CombinedSelectColumnsPhase<R> select() {
    CombinedSelectColumnsPhase<R> right = new CombinedSelectColumnsPhase<R>(this.op.getLeft(), false);
    this.op.setRight(right.getSelect());
    return right;
  }

  public CombinedSelectColumnsPhase<R> selectDistinct() {
    CombinedSelectColumnsPhase<R> right = new CombinedSelectColumnsPhase<R>(this.op.getLeft(), true);
    this.op.setRight(right.getSelect());
    return right;
  }

  public CombinedSelectColumnsPhase<R> select(final ResultSetColumn... resultSetColumns) {
    CombinedSelectColumnsPhase<R> right = new CombinedSelectColumnsPhase<R>(this.op.getLeft(), false, resultSetColumns);
    this.op.setRight(right.getSelect());
    return right;
  }

  public CombinedSelectColumnsPhase<R> selectDistinct(final ResultSetColumn... resultSetColumns) {
    CombinedSelectColumnsPhase<R> right = new CombinedSelectColumnsPhase<R>(this.op.getLeft(), true, resultSetColumns);
    this.op.setRight(right.getSelect());
    return right;
  }

}
