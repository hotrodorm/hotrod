package org.hotrod.runtime.livesql.queries.select;

import java.util.Arrays;

import org.hotrod.runtime.livesql.expressions.ComparableExpression;
import org.hotrod.runtime.livesql.expressions.OrderingTerm;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.select.sets.CombinedSelectObject;
import org.hotrod.runtime.livesql.queries.select.sets.IndividualSelectPhase;

public class SelectGroupByPhase<R> extends IndividualSelectPhase<R> {

  // Constructor

  SelectGroupByPhase(final LiveSQLContext context, final CombinedSelectObject<R> combined,
      final ComparableExpression... expressions) {
    super(context, combined);
    this.getLastSelect().setGroupBy(Arrays.asList(expressions));
  }

  // Next phases

  public SelectHavingPhase<R> having(final Predicate predicate) {
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
