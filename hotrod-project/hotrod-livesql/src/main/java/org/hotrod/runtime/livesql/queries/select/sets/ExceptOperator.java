package org.hotrod.runtime.livesql.queries.select.sets;

import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class ExceptOperator<R> extends SetOperator<R> {

  public ExceptOperator() {
    super();
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.getSQLDialect().getSetOperationRenderer().renderExcept(w);
  }

  @Override
  public int getPrecedence() {
    return PRECEDENCE_EXCEPT;
  }

}
