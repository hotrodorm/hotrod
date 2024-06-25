package org.hotrod.runtime.livesql.queries.select;

import org.hotrod.runtime.livesql.expressions.ComparableExpression;
import org.hotrod.runtime.livesql.expressions.OrderingTerm;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.select.sets.CombinedSelectObject;
import org.hotrod.runtime.livesql.queries.select.sets.IndividualSelectPhase;

public class NonLockableSelectWherePhase<R> extends IndividualSelectPhase<R> {

  // Constructors

  NonLockableSelectWherePhase(final LiveSQLContext context, final CombinedSelectObject<R> combined,
      final Predicate predicate) {
    super(context, combined);
    this.getLastSelect().setWhereCondition(predicate);
  }

  // Next phases

  public SelectGroupByPhase<R> groupBy(final ComparableExpression... columns) {
    return new SelectGroupByPhase<R>(this.context, this.combined, columns);
  }

  public SelectOrderByPhase<R> orderBy(final OrderingTerm... orderingTerms) {
    return new SelectOrderByPhase<R>(this.context, this.combined, orderingTerms);
  }

  public SelectOffsetPhase<R> offset(final int offset) {
    return new SelectOffsetPhase<R>(this.context, this.combined, offset);
  }

  public SelectLimitPhase<R> limit(final int limit) {
    return new SelectLimitPhase<R>(this.context, this.combined, limit);
  }

}
