package org.hotrod.runtime.livesql.queries.select;

import java.util.List;

import org.hotrod.runtime.cursors.Cursor;

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

  public Cursor<T> executeCursor() {
    return this.select.executeCursor();
  }

}
