package org.hotrod.runtime.livesql.queries.select;

import org.hotrod.runtime.livesql.queries.LiveSQLContext;

public class CriteriaLimitPhase<T> extends CriteriaPhase<T> {

  public CriteriaLimitPhase(final LiveSQLContext context, final AbstractSelectObject<T> select) {
    super(context, select);
  }

  // next phases

  public CriteriaForUpdatePhase<T> forUpdate() {
    this.select.setForUpdate();
    return new CriteriaForUpdatePhase<T>(this.context, this.select);
  }

  public CriteriaForUpdatePhase<T> forShare() {
    this.select.setForShare();
    return new CriteriaForUpdatePhase<T>(this.context, this.select);
  }

}
