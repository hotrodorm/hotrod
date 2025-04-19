package org.hotrod.runtime.livesql.queries.select;

import org.hotrod.dynamicsql.RowReader;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;

public class CriteriaForUpdateConcurrencyPhase<T> extends CriteriaPhase<T> {

  public CriteriaForUpdateConcurrencyPhase(final LiveSQLContext context, final AbstractSelectObject<T> select,
      RowReader<T> rowReader, final Number waitTime, final boolean skipLocked) {
    super(context, select, rowReader);
    select.setLockingConcurrency(waitTime, skipLocked);
  }

  // next phases

}
