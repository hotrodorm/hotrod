package org.hotrod.livesql.queries.select;

import java.util.List;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.SQLExpression;
import org.hotrod.livesql.queries.LiveSQLContext;
import org.hotrod.livesql.queries.ctes.CTE;

public class NonLockableSelectDistinctOnPhase<R> {

  private LiveSQLContext context;
  private List<CTE> ctes;
  private Expression[] distinctOn;

  // Constructor

  public NonLockableSelectDistinctOnPhase(final LiveSQLContext context, final List<CTE> ctes,
      final Expression... distinctOn) {
    this.context = context;
    this.ctes = ctes;
    this.distinctOn = distinctOn;
  }

  // Next phases

  public NonLockableSelectColumnsPhase<R> columns(final SQLExpression... resultSetColumns) {
    return new NonLockableSelectColumnsPhase<R>(this.context, ctes, distinctOn, resultSetColumns);
  }

  public NonLockableSelectFromPhase<R> from(final TableExpression tableViewOrSubquery) {
    NonLockableSelectColumnsPhase<R> p = new NonLockableSelectColumnsPhase<R>(this.context, ctes, distinctOn);
    return p.from(tableViewOrSubquery);
  }

}
