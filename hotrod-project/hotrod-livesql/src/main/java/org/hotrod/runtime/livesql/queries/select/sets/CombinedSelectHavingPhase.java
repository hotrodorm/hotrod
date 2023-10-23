package org.hotrod.runtime.livesql.queries.select.sets;

import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;

public class CombinedSelectHavingPhase<R> extends CombinedSelectPhase<R> {

  // Constructor

  CombinedSelectHavingPhase(final LiveSQLContext context, final CombinedSelectObject<R> combined,
      final Predicate predicate) {
    super(context, combined);
    if (predicate != null) {
      this.getLastSelect().setHavingCondition(predicate);
    }
  }

  // This phase

  // Next phases

}
