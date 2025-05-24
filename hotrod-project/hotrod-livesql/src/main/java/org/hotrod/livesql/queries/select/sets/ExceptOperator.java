package org.hotrod.livesql.queries.select.sets;

import org.hotrod.livesql.queries.QueryWriter;

public class ExceptOperator extends SetOperator {

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
