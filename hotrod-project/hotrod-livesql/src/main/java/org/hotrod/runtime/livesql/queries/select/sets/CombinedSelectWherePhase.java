package org.hotrod.runtime.livesql.queries.select.sets;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;

public class CombinedSelectWherePhase<R> extends CombinedSelectPhase<R> {

  // Constructors

  CombinedSelectWherePhase(final LiveSQLContext context, final CombinedSelectObject<R> combined,
      final Predicate predicate) {
    super(context, combined);
    this.getLastSelect().setWhereCondition(predicate);
  }

  // Next phases

  public CombinedSelectGroupByPhase<R> groupBy(final Expression... columns) {
    return new CombinedSelectGroupByPhase<R>(this.context, this.combined, columns);
  }

}
