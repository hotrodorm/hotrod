package org.hotrod.runtime.livesql.queries.select;

import org.hotrod.runtime.livesql.queries.LiveSQLContext;

public class CriteriaForUpdateConcurrencyPhase<T> extends CriteriaPhase<T> {

  public CriteriaForUpdateConcurrencyPhase(final LiveSQLContext context, final AbstractSelectObject<T> select,
      final String mapperStatement, final Number waitTime, final boolean skipLocked) {
    super(context, select, mapperStatement);
    select.setLockingConcurrency(waitTime, skipLocked);
  }

  // next phases

}
