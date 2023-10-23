package org.hotrod.runtime.livesql.queries.select.sets;

import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class UnionAllOperator extends SetOperator {

  public UnionAllOperator() {
    super();
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.getSQLDialect().getSetOperationRenderer().renderUnionAll(w);
  }

  @Override
  public int getPrecedence() {
    return PRECEDENCE_UNION_ALL;
  }

}
