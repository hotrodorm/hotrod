package org.hotrod.runtime.livesql.queries.select;

import org.hotrod.runtime.livesql.queries.LiveSQLContext;

public class CriteriaForUpdatePhase<T> extends CriteriaPhase<T> {

  public CriteriaForUpdatePhase(final LiveSQLContext context, final AbstractSelectObject<T> select,
      final String mapperStatement) {
    super(context, select, mapperStatement);
  }

  // next phases

  public CriteriaForUpdateConcurrencyPhase<T> noWait() {
    return new CriteriaForUpdateConcurrencyPhase<>(super.context, this.select, this.mapperStatement, null, false);
  }

  public CriteriaForUpdateConcurrencyPhase<T> wait(final int time) {
    return new CriteriaForUpdateConcurrencyPhase<>(super.context, this.select, this.mapperStatement, time, false);
  }

  public CriteriaForUpdateConcurrencyPhase<T> wait(final double time) {
    return new CriteriaForUpdateConcurrencyPhase<>(super.context, this.select, this.mapperStatement, time, false);
  }

  public CriteriaForUpdateConcurrencyPhase<T> skipLocked() {
    return new CriteriaForUpdateConcurrencyPhase<>(super.context, this.select, this.mapperStatement, null, true);
  }

}
