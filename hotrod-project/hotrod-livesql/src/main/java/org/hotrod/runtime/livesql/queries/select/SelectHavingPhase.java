package org.hotrod.runtime.livesql.queries.select;

import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.ordering.OrderingTerm;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.select.sets.IndividualSelectPhase;

public class SelectHavingPhase<R> extends IndividualSelectPhase<R> {

  // Constructor

  SelectHavingPhase(final LiveSQLContext context, final SelectObject<R> select, final Predicate predicate) {
    super(context, select);
    if (predicate != null) {
      this.select.setHavingCondition(predicate);
    }
  }

  // Same phase

  // Next phases

  public SelectOrderByPhase<R> orderBy(final OrderingTerm... orderingTerms) {
    return new SelectOrderByPhase<R>(this.context, this.select, orderingTerms);
  }

  public SelectOffsetPhase<R> offset(final int offset) {
    return new SelectOffsetPhase<R>(this.context, this.select, offset);
  }

  public SelectLimitPhase<R> limit(final int limit) {
    return new SelectLimitPhase<R>(this.context, this.select, limit);
  }

}
