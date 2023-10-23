package org.hotrod.runtime.livesql.queries.select;

import java.util.Arrays;

import org.hotrod.runtime.livesql.ordering.OrderingTerm;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.select.sets.AbstractSelectPhase;
import org.hotrod.runtime.livesql.queries.select.sets.CombinedSelectObject;

public class SelectOrderByPhase<R> extends AbstractSelectPhase<R> {

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
