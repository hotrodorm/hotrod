package org.hotrod.livesql.queries.select.sets;

import java.util.Arrays;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.predicates.GeneralBooleanExpression;
import org.hotrod.livesql.queries.LiveSQLContext;

public class CombinedSelectGroupByPhase<R> extends CombinedSelectPhase<R> {

  // Constructor

  CombinedSelectGroupByPhase(final LiveSQLContext context, final CombinedSelectObject<R> combined,
      final ComparableExpression... expressions) {
    super(context, combined);
    this.getLastSelect().setGroupBy(Arrays.asList(expressions));
  }

  // Next stages

  public CombinedSelectHavingPhase<R> having(final GeneralBooleanExpression predicate) {
    return new CombinedSelectHavingPhase<R>(this.context, this.combined, predicate);
  }

}
