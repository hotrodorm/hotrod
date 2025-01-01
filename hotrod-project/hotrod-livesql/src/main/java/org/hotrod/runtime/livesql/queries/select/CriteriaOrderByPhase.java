package org.hotrod.runtime.livesql.queries.select;

import org.hotrod.dynamicsql.PreparedSelectQuery.RowReader;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;

public class CriteriaOrderByPhase<T> extends CriteriaPhase<T> {

  public CriteriaOrderByPhase(final LiveSQLContext context, final AbstractSelectObject<T> select,
      RowReader<T> rowReader) {
    super(context, select, rowReader);
  }

  // next phases

  public CriteriaOffsetPhase<T> offset(final int offset) {
    this.select.setOffset(offset);
    return new CriteriaOffsetPhase<T>(this.context, this.select, this.rowReader);
  }

  public CriteriaLimitPhase<T> limit(final int limit) {
    this.select.setLimit(limit);
    return new CriteriaLimitPhase<T>(this.context, this.select, this.rowReader);
  }

  public CriteriaForUpdatePhase<T> forUpdate() {
    this.select.setForUpdate();
    return new CriteriaForUpdatePhase<T>(this.context, this.select, this.rowReader);
  }

  public CriteriaForUpdatePhase<T> forShare() {
    this.select.setForShare();
    return new CriteriaForUpdatePhase<T>(this.context, this.select, this.rowReader);
  }

}
