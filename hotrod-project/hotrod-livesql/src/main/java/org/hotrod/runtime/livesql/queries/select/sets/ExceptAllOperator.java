package org.hotrod.runtime.livesql.queries.select.sets;

import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class ExceptAllOperator<R> extends SetOperator<R> {

  public ExceptAllOperator() {
    super();
  }

  @Override
  protected void renderSetOperator(QueryWriter w) {
    w.write("EXCEPT ALL");
  }

  @Override
  public int getPrecedence() {
    return PRECEDENCE_EXCEPT_ALL;
  }

}