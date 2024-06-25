package org.hotrod.runtime.livesql.queries.select;

import java.util.Arrays;

import org.hotrod.runtime.livesql.expressions.OrderingTerm;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.select.sets.CombinedSelectObject;
import org.hotrod.runtime.livesql.queries.select.sets.IndividualSelectPhase;

public class SelectOrderByPhase<R> extends IndividualSelectPhase<R> {

  // Constructor

  SelectOrderByPhase(final LiveSQLContext context, final CombinedSelectObject<R> combined,
      final OrderingTerm... orderingTerms) {
    super(context, combined);
    this.getLastSelect().setColumnOrderings(Arrays.asList(orderingTerms));
  }

  // Same phase

  // Next phases

  public SelectOffsetPhase<R> offset(final int offset) {
    return new SelectOffsetPhase<R>(this.context, this.combined, offset);
  }

  public SelectLimitPhase<R> limit(final int limit) {
    return new SelectLimitPhase<R>(this.context, this.combined, limit);
  }

}
