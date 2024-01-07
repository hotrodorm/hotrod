package org.hotrod.runtime.livesql.queries.select;

import org.hotrod.runtime.livesql.queries.LiveSQLContext;

public class CriteriaOrderByPhase<T> extends CriteriaPhase<T> {

  public CriteriaOrderByPhase(final LiveSQLContext context, final AbstractSelectObject<T> select,
      final String mapperStatement) {
    super(context, select, mapperStatement);
  }

  // next phases

  public CriteriaOffsetPhase<T> offset(final int offset) {
    this.select.setOffset(offset);
    return new CriteriaOffsetPhase<T>(this.context, this.select, this.mapperStatement);
  }

  public CriteriaLimitPhase<T> limit(final int limit) {
    this.select.setLimit(limit);
    return new CriteriaLimitPhase<T>(this.context, this.select, this.mapperStatement);
  }

  public CriteriaForUpdatePhase<T> forUpdate() {
    this.select.setForUpdate();
    return new CriteriaForUpdatePhase<T>(this.context, this.select, this.mapperStatement);
  }

}
