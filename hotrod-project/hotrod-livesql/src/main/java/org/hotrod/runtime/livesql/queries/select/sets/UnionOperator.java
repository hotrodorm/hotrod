package org.hotrod.runtime.livesql.queries.select.sets;

import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class UnionOperator<R> extends SetOperator<R> {

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
