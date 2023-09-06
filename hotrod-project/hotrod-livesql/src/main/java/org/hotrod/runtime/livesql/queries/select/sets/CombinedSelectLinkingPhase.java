package org.hotrod.runtime.livesql.queries.select.sets;

import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;

public class CombinedSelectLinkingPhase<R> {

  private SetOperator<R> op;
  private LiveSQLContext context;

  public CombinedSelectLinkingPhase(final SetOperator<R> op, final LiveSQLContext context) {
    this.op = op;
    this.context = context;
  }

  // Select

  public CombinedSelectColumnsPhase<R> select() {
    CombinedSelectColumnsPhase<R> cs = new CombinedSelectColumnsPhase<R>(this.context, null, false);
    this.op.add(cs.getSelect());
    return cs;
  }

  public CombinedSelectColumnsPhase<R> selectDistinct() {
    CombinedSelectColumnsPhase<R> cs = new CombinedSelectColumnsPhase<R>(this.context, null, true);
    this.op.add(cs.getSelect());
    return cs;
  }

  public CombinedSelectColumnsPhase<R> select(final ResultSetColumn... resultSetColumns) {
    CombinedSelectColumnsPhase<R> cs = new CombinedSelectColumnsPhase<R>(this.context, null, false, resultSetColumns);
    this.op.add(cs.getSelect());
    return cs;
  }

  public CombinedSelectColumnsPhase<R> selectDistinct(final ResultSetColumn... resultSetColumns) {
    CombinedSelectColumnsPhase<R> cs = new CombinedSelectColumnsPhase<R>(this.context, null, true, resultSetColumns);
    this.op.add(cs.getSelect());
    return cs;
  }

}
