package org.hotrod.livesql.queries.select.sets;

import org.hotrod.livesql.queries.QueryWriter;

public class ExceptAllOperator extends SetOperator {

  public ExceptAllOperator() {
    super();
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.getSQLDialect().getSetOperationRenderer().renderExceptAll(w);
  }

  @Override
  public int getPrecedence() {
    return PRECEDENCE_EXCEPT_ALL;
  }

}
