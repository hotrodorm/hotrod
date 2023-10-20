package org.hotrod.runtime.livesql.queries.select;

import java.util.Arrays;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.ordering.OrderingTerm;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.select.sets.IndividualSelectPhase;

public class SelectGroupByPhase<R> extends IndividualSelectPhase<R> {

  // Constructor

  SelectGroupByPhase(final LiveSQLContext context, final SelectObject<R> select, final Expression... expressions) {
    super(context, select);
    this.select.setGroupBy(Arrays.asList(expressions));
  }

  // Next phases

  public SelectHavingPhase<R> having(final Predicate predicate) {
    return new SelectHavingPhase<R>(this.context, this.select, predicate);
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
