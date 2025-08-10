package org.hotrod.livesql.queries.select.sets;

import org.hotrod.livesql.expressions.bool.BooleanExpression;
import org.hotrod.livesql.queries.LiveSQLContext;

public class CombinedSelectHavingPhase<R> extends CombinedSelectPhase<R> {

  // Constructor

  CombinedSelectHavingPhase(final LiveSQLContext context, final CombinedSelectObject<R> combined,
      final BooleanExpression predicate) {
    super(context, combined);
    if (predicate != null) {
      this.getLastSelect().setHavingCondition(predicate);
    }
  }

  // This phase

  // Next phases

}
