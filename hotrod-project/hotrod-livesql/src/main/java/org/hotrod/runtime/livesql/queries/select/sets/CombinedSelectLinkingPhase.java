package org.hotrod.runtime.livesql.queries.select.sets;

import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.select.SelectObject;

public class CombinedSelectLinkingPhase<R> {

  private LiveSQLContext context;
  private SelectObject<R> so;
  private CombinedSelectObject<R> cm;
  private SetOperator<R> op;

  public CombinedSelectLinkingPhase(final LiveSQLContext context, final SelectObject<R> so, final SetOperator<R> op) {
    this.context = context;
    this.so = so;
    this.cm = null;
    this.op = op;
    System.out.println("Linking 1:");
  }

  public CombinedSelectLinkingPhase(final LiveSQLContext context, final CombinedSelectObject<R> cm,
      final SetOperator<R> op) {
    this.context = context;
    this.so = null;
    this.cm = cm;
    this.op = op;
    System.out.println("Linking 2: cm=" + this.cm.toString());
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

    CombinedSelectObject<R> targetObj;
    if (this.so != null) {
      targetObj = new CombinedSelectObject<>(this.so);
      this.so.setParent(targetObj);
    } else {
      targetObj = this.cm.prepareCombinationWith(this.op);
    }

    CombinedSelectColumnsPhase<R> cs = new CombinedSelectColumnsPhase<>(this.context, null, distinct, targetObj,
        resultSetColumns);
    cs.getSelect().setParent(targetObj);
    targetObj.add(new SetOperatorTerm<>(this.op, cs.getSelect()));
    System.out.println("Resulting: " + targetObj.toString());
    return cs;

  }

}
