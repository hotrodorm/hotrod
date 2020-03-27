package org.hotrod.runtime.livesql.queries.select;

import java.util.List;

public class CriteriaOrderByPhase<T> {

  private AbstractSelect<T> select;

  CriteriaOrderByPhase(final AbstractSelect<T> select) {
    this.select = select;
  }

  // next phases

  public CriteriaOffsetPhase<T> offset(final int offset) {
    this.offset(offset);
    return new CriteriaOffsetPhase<T>(this.select);
  }

  public CriteriaLimitPhase<T> limit(final int limit) {
    this.limit(limit);
    return new CriteriaLimitPhase<T>(this.select);
  }

  // execute

  public List<T> execute() {
    return this.select.execute();
  }

}
