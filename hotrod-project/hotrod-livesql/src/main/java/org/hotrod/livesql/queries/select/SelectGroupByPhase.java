package org.hotrod.livesql.queries.select;

import java.util.Arrays;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.predicates.GeneralBooleanExpression;
import org.hotrod.livesql.ordering.OrderingTerm;
import org.hotrod.livesql.queries.LiveSQLContext;
import org.hotrod.livesql.queries.select.sets.CombinedSelectObject;
import org.hotrod.livesql.queries.select.sets.IndividualSelectPhase;

public class SelectGroupByPhase<R> extends IndividualSelectPhase<R> {

  // Constructor

  SelectGroupByPhase(final LiveSQLContext context, final CombinedSelectObject<R> combined,
      final ComparableExpression... expressions) {
    super(context, combined);
    this.getLastSelect().setGroupBy(Arrays.asList(expressions));
  }

  // Next phases

  public SelectHavingPhase<R> having(final GeneralBooleanExpression predicate) {
    return new SelectHavingPhase<R>(this.context, this.combined, predicate);
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
