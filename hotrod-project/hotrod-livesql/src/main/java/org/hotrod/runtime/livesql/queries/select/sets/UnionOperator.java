package org.hotrod.runtime.livesql.queries.select.sets;

import org.hotrod.runtime.livesql.queries.QueryWriter;

public class UnionOperator extends SetOperator {

  public UnionOperator() {
    super();
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.getSQLDialect().getSetOperationRenderer().renderUnion(w);
  }

  @Override
  public int getPrecedence() {
    return PRECEDENCE_UNION;
  }

}
