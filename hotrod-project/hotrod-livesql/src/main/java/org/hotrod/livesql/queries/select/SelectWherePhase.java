package org.hotrod.livesql.queries.select;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.bool.GeneralBooleanExpression;
import org.hotrod.livesql.ordering.OrderingTerm;
import org.hotrod.livesql.queries.LiveSQLContext;
import org.hotrod.livesql.queries.select.sets.CombinedSelectObject;
import org.hotrod.livesql.queries.select.sets.LockableSelectPhase;

public class SelectWherePhase<R> extends LockableSelectPhase<R> {

  // Constructors

  SelectWherePhase(final LiveSQLContext context, final CombinedSelectObject<R> combined, final GeneralBooleanExpression predicate) {
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
