package org.hotrod.runtime.livesql.queries.select.sets;

import java.util.Arrays;

import org.hotrod.runtime.livesql.ordering.CombinedOrderingTerm;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;

public class CombinedSelectOrderByPhase<R> extends AbstractSelectPhase<R> {

  // Constructor

  CombinedSelectOrderByPhase(final LiveSQLContext context, final CombinedSelectObject<R> combined,
      final CombinedOrderingTerm... orderingTerms) {
    super(context, combined);
    this.getLastSelect().setColumnOrderings(Arrays.asList(orderingTerms));
  }

  // Same phase

  // Next phases

  public CombinedSelectOffsetPhase<R> offset(final int offset) {
    return new CombinedSelectOffsetPhase<R>(this.context, this.combined, offset);
  }

  public CombinedSelectLimitPhase<R> limit(final int limit) {
    return new CombinedSelectLimitPhase<R>(this.context, this.combined, limit);
  }

}
