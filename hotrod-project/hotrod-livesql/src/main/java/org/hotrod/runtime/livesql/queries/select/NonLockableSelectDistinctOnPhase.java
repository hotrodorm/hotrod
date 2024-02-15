package org.hotrod.runtime.livesql.queries.select;

import java.util.List;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.ctes.CTE;

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

  public NonLockableSelectColumnsPhase<R> columns(final ResultSetColumn... resultSetColumns) {
    return new NonLockableSelectColumnsPhase<R>(this.context, ctes, distinctOn, resultSetColumns);
  }

  public NonLockableSelectFromPhase<R> from(final TableExpression tableViewOrSubquery) {
    NonLockableSelectColumnsPhase<R> p = new NonLockableSelectColumnsPhase<R>(this.context, ctes, distinctOn);
    return p.from(tableViewOrSubquery);
  }

}
