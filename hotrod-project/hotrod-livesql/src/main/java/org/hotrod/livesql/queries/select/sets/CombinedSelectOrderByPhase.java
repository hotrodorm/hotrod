package org.hotrod.livesql.queries.select.sets;

import java.util.Arrays;

import org.hotrod.livesql.ordering.CombinedOrderingTerm;
import org.hotrod.livesql.queries.LiveSQLContext;

public class CombinedSelectOrderByPhase<R> extends AbstractSelectPhase<R> {

  // Constructor

  CombinedSelectOrderByPhase(final LiveSQLContext context, final CombinedSelectObject<R> combined,
      final CombinedOrderingTerm... orderingTerms) {
    super(context, combined);
    combined.setColumnOrderings(Arrays.asList(orderingTerms));
//    System.out.println(">>> COMBINED ORDERING on: " + combined);
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
