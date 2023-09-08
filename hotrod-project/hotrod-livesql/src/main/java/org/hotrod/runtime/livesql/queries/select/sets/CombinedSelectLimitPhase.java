package org.hotrod.runtime.livesql.queries.select.sets;

import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.select.SelectObject;

public class CombinedSelectLimitPhase<R> extends AbstractSelectPhase<R> {

  // Constructor

  CombinedSelectLimitPhase(final LiveSQLContext context, final SelectObject<R> select, final int limit) {
    super(context, select);
    this.select.setLimit(limit);
  }

}
