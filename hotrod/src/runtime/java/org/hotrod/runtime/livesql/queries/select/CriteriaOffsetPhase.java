package org.hotrod.runtime.livesql.queries.select;

import java.util.List;

public class CriteriaOffsetPhase<T> {

  private AbstractSelect<T> select;

  CriteriaOffsetPhase(final AbstractSelect<T> select) {
    this.select = select;
  }

  // next phases

  public CriteriaLimitPhase<T> limit(final int limit) {
    this.limit(limit);
    return new CriteriaLimitPhase<T>(this.select);
  }

  // execute

  public List<T> execute() {
    return this.select.execute();
  }

}
