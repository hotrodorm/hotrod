package org.hotrod.runtime.livesql.queries.select.sets;

import java.util.List;

import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.ctes.CTE;
import org.hotrod.runtime.livesql.queries.select.SelectForUpdatePhase;

public class LockableSelectPhase<R> extends IndividualSelectPhase<R> {

  public LockableSelectPhase(final LiveSQLContext context, final List<CTE> ctes, final boolean distinct,
      final boolean doNotAliasColumns) {
    super(context, ctes, distinct, doNotAliasColumns);
  }

  public LockableSelectPhase(final LiveSQLContext context, final CombinedSelectObject<R> cs) {
    super(context, cs);
  }

  // Next Phases

  public SelectForUpdatePhase<R> forUpdate() {
    return new SelectForUpdatePhase<R>(this.context, this.combined);
  }

}
