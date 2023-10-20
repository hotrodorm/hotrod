package org.hotrod.runtime.livesql.queries.select;

import java.util.Arrays;

import org.hotrod.runtime.livesql.ordering.OrderingTerm;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.select.sets.AbstractSelectPhase;

public class SelectOrderByPhase<R> extends AbstractSelectPhase<R> {

  // Constructor

  SelectOrderByPhase(final LiveSQLContext context, final SelectObject<R> select, final OrderingTerm... orderingTerms) {
    super(context, select);
    this.select.setColumnOrderings(Arrays.asList(orderingTerms));
  }

  // Same phase

  // Next phases

  public SelectOffsetPhase<R> offset(final int offset) {
    return new SelectOffsetPhase<R>(this.context, this.select, offset);
  }

  public SelectLimitPhase<R> limit(final int limit) {
    return new SelectLimitPhase<R>(this.context, this.select, limit);
  }

}
