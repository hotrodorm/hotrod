package org.hotrod.runtime.livesql.queries.select.sets;

import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;

public class CombinedSelectLinkingPhase<R> {

  private LiveSQLContext context;
  private CombinedSelectObject<R> combined;
  private SetOperator op;

  public CombinedSelectLinkingPhase(final LiveSQLContext context, final CombinedSelectObject<R> combined,
      final SetOperator op) {
    this.context = context;
    this.combined = combined;
    this.op = op;
//    System.out.println("- ini: " + this.combined.toString());
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
//    System.out.println("- pre: " + this.combined.toString());
    CombinedSelectObject<R> newCS = this.combined.prepareCombinationWith(this.op);
    CombinedSelectColumnsPhase<R> ph = new CombinedSelectColumnsPhase<>(this.context, null, distinct, newCS,
        resultSetColumns);
    newCS.add(this.op, ph.getLastSelect());
    ph.getLastSelect().setParent(newCS);
    ph.setCombinedSelectObject(newCS);
//    System.out.println("+ Resulting: " + newCS.toString());
//    System.out.println("+ Resulting (p): " + ph.getCombinedSelect().toString());
    return ph;
  }

}
