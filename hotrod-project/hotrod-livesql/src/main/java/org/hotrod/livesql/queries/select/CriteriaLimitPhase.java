package org.hotrod.livesql.queries.select;

import org.hotrod.dynamicsql.RowReader;
import org.hotrod.livesql.queries.LiveSQLContext;

public class CriteriaLimitPhase<T> extends CriteriaPhase<T> {

  public CriteriaLimitPhase(final LiveSQLContext context, final AbstractSelectObject<T> select,
      RowReader<T> rowReader) {
    super(context, select, rowReader);
  }

  // next phases

  public CriteriaForUpdatePhase<T> forUpdate() {
    this.select.setForUpdate();
    return new CriteriaForUpdatePhase<T>(this.context, this.select, this.rowReader);
  }

  public CriteriaForUpdatePhase<T> forShare() {
    this.select.setForShare();
    return new CriteriaForUpdatePhase<T>(this.context, this.select, this.rowReader);
  }

}
