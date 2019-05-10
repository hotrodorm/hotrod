package org.hotrod.runtime.livesql;

import java.util.List;

public class CriteriaLimitPhase<T> {

  private AbstractSelect<T> select;

  CriteriaLimitPhase(final AbstractSelect<T> select) {
    this.select = select;
  }

  // next phases

  // execute

  public List<T> execute() {
    return this.select.execute();
  }

}
