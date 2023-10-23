package org.hotrod.runtime.livesql.queries.select.sets;

import java.util.Arrays;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;

public class CombinedSelectGroupByPhase<R> extends CombinedSelectPhase<R> {

  // Constructor

  CombinedSelectGroupByPhase(final LiveSQLContext context, final CombinedSelectObject<R> combined,
      final Expression... expressions) {
    super(context, combined);
    this.getLastSelect().setGroupBy(Arrays.asList(expressions));
  }

  // Next stages

  public CombinedSelectHavingPhase<R> having(final Predicate predicate) {
    return new CombinedSelectHavingPhase<R>(this.context, this.combined, predicate);
  }

}
