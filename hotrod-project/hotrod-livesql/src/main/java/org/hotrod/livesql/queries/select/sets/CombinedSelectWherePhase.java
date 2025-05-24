package org.hotrod.livesql.queries.select.sets;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.predicates.GeneralBooleanExpression;
import org.hotrod.livesql.queries.LiveSQLContext;

public class CombinedSelectWherePhase<R> extends CombinedSelectPhase<R> {

  // Constructors

  CombinedSelectWherePhase(final LiveSQLContext context, final CombinedSelectObject<R> combined,
      final GeneralBooleanExpression predicate) {
    super(context, combined);
    this.getLastSelect().setWhereCondition(predicate);
  }

  // Next phases

  public CombinedSelectGroupByPhase<R> groupBy(final ComparableExpression... columns) {
    return new CombinedSelectGroupByPhase<R>(this.context, this.combined, columns);
  }

}
