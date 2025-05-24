package org.hotrod.livesql.queries.select;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.predicates.GeneralBooleanExpression;
import org.hotrod.livesql.ordering.OrderingTerm;
import org.hotrod.livesql.queries.LiveSQLContext;
import org.hotrod.livesql.queries.select.sets.CombinedSelectObject;
import org.hotrod.livesql.queries.select.sets.IndividualSelectPhase;

public class NonLockableSelectWherePhase<R> extends IndividualSelectPhase<R> {

  // Constructors

  NonLockableSelectWherePhase(final LiveSQLContext context, final CombinedSelectObject<R> combined,
      final GeneralBooleanExpression predicate) {
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
