package org.hotrod.runtime.livesql.queries.select;

import org.hotrod.runtime.livesql.expressions.ComparableExpression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.ordering.OrderingTerm;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.select.sets.CombinedSelectObject;
import org.hotrod.runtime.livesql.queries.select.sets.LockableSelectPhase;

public class SelectWherePhase<R> extends LockableSelectPhase<R> {

  // Constructors

  SelectWherePhase(final LiveSQLContext context, final CombinedSelectObject<R> combined, final Predicate predicate) {
    super(context, combined);
    this.getLastSelect().setWhereCondition(predicate);
  }

  // Next phases

  public SelectGroupByPhase<R> groupBy(final ComparableExpression... columns) {
    return new SelectGroupByPhase<R>(this.context, this.combined, columns);
  }

  public LockableSelectOrderByPhase<R> orderBy(final OrderingTerm... orderingTerms) {
    return new LockableSelectOrderByPhase<R>(this.context, this.combined, orderingTerms);
  }

  public LockableSelectOffsetPhase<R> offset(final int offset) {
    return new LockableSelectOffsetPhase<R>(this.context, this.combined, offset);
  }

  public LockableSelectLimitPhase<R> limit(final int limit) {
    return new LockableSelectLimitPhase<R>(this.context, this.combined, limit);
  }

}
