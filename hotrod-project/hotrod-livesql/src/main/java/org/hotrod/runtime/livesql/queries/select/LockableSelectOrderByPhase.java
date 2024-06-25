package org.hotrod.runtime.livesql.queries.select;

import java.util.Arrays;

import org.hotrod.runtime.livesql.expressions.OrderingTerm;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.select.sets.CombinedSelectObject;
import org.hotrod.runtime.livesql.queries.select.sets.LockableSelectPhase;

public class LockableSelectOrderByPhase<R> extends LockableSelectPhase<R> {

  // Constructor

  LockableSelectOrderByPhase(final LiveSQLContext context, final CombinedSelectObject<R> combined,
      final OrderingTerm... orderingTerms) {
    super(context, combined);
    this.getLastSelect().setColumnOrderings(Arrays.asList(orderingTerms));
  }

  // Same phase

  // Next phases

  public LockableSelectOffsetPhase<R> offset(final int offset) {
    return new LockableSelectOffsetPhase<R>(this.context, this.combined, offset);
  }

  public LockableSelectLimitPhase<R> limit(final int limit) {
    return new LockableSelectLimitPhase<R>(this.context, this.combined, limit);
  }

}
