package org.hotrod.runtime.livesql.queries.select.sets;

import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;

public class CombinedSelectLinkingPhase<R> {

  private LiveSQLContext context;
  private CombinedSelectObject<R> cs;
  private SetOperator<R> op;

  public CombinedSelectLinkingPhase(final LiveSQLContext context, final CombinedSelectObject<R> cs,
      final SetOperator<R> op) {
    this.context = context;
    this.cs = cs;
    this.op = op;
  }

  // Select

  public CombinedSelectColumnsPhase<R> select() {
    return preparePhase(false);
  }

  public CombinedSelectColumnsPhase<R> selectDistinct() {
    return preparePhase(true);
  }

  public CombinedSelectColumnsPhase<R> select(final ResultSetColumn... resultSetColumns) {
    return preparePhase(false, resultSetColumns);
  }

  public CombinedSelectColumnsPhase<R> selectDistinct(final ResultSetColumn... resultSetColumns) {
    return preparePhase(true, resultSetColumns);
  }

  private CombinedSelectColumnsPhase<R> preparePhase(final boolean distinct,
      final ResultSetColumn... resultSetColumns) {
    CombinedSelectObject<R> newCS = this.cs.prepareCombinationWith(this.op);
    CombinedSelectColumnsPhase<R> ph = new CombinedSelectColumnsPhase<>(this.context, null, distinct, newCS,
        resultSetColumns);
    newCS.add(new SetOperatorTerm<>(this.op, ph.getSelect()));
    System.out.println("Resulting: " + newCS.toString());
    return ph;
  }

}
