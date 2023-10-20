package org.hotrod.runtime.livesql.queries.select;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.ordering.OrderingTerm;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.select.sets.IndividualSelectPhase;

public class SelectWherePhase<R> extends IndividualSelectPhase<R> {

  // Constructors

  SelectWherePhase(final LiveSQLContext context, final SelectObject<R> select, final Predicate predicate) {
    super(context, select);
    this.select.setWhereCondition(predicate);
  }

  // Next phases

  public SelectGroupByPhase<R> groupBy(final Expression... columns) {
    return new SelectGroupByPhase<R>(this.context, this.select, columns);
  }

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
