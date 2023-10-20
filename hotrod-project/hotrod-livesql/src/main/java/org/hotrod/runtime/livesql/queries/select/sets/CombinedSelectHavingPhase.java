package org.hotrod.runtime.livesql.queries.select.sets;

import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.ordering.CombinedOrderingTerm;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.select.SelectObject;

public class CombinedSelectHavingPhase<R> extends CombinedSelectPhase<R> {

  // Constructor

  CombinedSelectHavingPhase(final LiveSQLContext context, final SelectObject<R> select, final Predicate predicate) {
    super(context, select);
    if (predicate != null) {
      this.select.setHavingCondition(predicate);
    }
  }

  // This phase

  // Next phases

  public CombinedSelectOrderByPhase<R> orderBy(final CombinedOrderingTerm... orderingTerms) {
    return new CombinedSelectOrderByPhase<R>(this.context, this.select, orderingTerms);
  }

  public CombinedSelectOffsetPhase<R> offset(final int offset) {
    return new CombinedSelectOffsetPhase<R>(this.context, this.select, offset);
  }

  public CombinedSelectLimitPhase<R> limit(final int limit) {
    return new CombinedSelectLimitPhase<R>(this.context, this.select, limit);
  }

}
