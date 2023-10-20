package org.hotrod.runtime.livesql.queries.select.sets;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.ordering.CombinedOrderingTerm;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.select.SelectObject;

public class CombinedSelectWherePhase<R> extends CombinedSelectPhase<R> {

  // Constructors

  CombinedSelectWherePhase(final LiveSQLContext context, final SelectObject<R> select, final Predicate predicate) {
    super(context, select);
    this.select.setWhereCondition(predicate);
  }

  // Next phases

  public CombinedSelectGroupByPhase<R> groupBy(final Expression... columns) {
    return new CombinedSelectGroupByPhase<R>(this.context, this.select, columns);
  }

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
