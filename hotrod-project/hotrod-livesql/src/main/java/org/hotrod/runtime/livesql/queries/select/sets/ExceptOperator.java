package org.hotrod.runtime.livesql.queries.select.sets;

import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class ExceptOperator<R> extends SetOperator<R> {

  public ExceptOperator() {
    super();
  }

  @Override
  protected void renderSetOperator(QueryWriter w) {
    w.write("EXCEPT");
  }

  @Override
  public int getPrecedence() {
    return PRECEDENCE_EXCEPT;
  }

}
