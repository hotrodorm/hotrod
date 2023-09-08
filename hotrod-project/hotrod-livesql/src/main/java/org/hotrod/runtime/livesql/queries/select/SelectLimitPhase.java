package org.hotrod.runtime.livesql.queries.select;

import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.select.sets.AbstractSelectPhase;

public class SelectLimitPhase<R> extends AbstractSelectPhase<R> {

  // Constructor

  SelectLimitPhase(final LiveSQLContext context, final SelectObject<R> select, final int limit) {
    super(context, select);
    this.select.setLimit(limit);
  }

}
