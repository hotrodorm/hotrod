package org.hotrod.runtime.livesql.queries.select.sets;

import java.util.Arrays;

import org.hotrod.runtime.livesql.ordering.CombinedOrderingTerm;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.select.SelectObject;

public class CombinedSelectOrderByPhase<R> extends AbstractSelectPhase<R> {

  // Constructor

  CombinedSelectOrderByPhase(final LiveSQLContext context, final SelectObject<R> select,
      final CombinedOrderingTerm... orderingTerms) {
    super(context, select);
    this.select.setColumnOrderings(Arrays.asList(orderingTerms));
  }

  // Same phase

  // Next phases

  public CombinedSelectOffsetPhase<R> offset(final int offset) {
    return new CombinedSelectOffsetPhase<R>(this.context, this.select, offset);
  }

  public CombinedSelectLimitPhase<R> limit(final int limit) {
    return new CombinedSelectLimitPhase<R>(this.context, this.select, limit);
  }

}
