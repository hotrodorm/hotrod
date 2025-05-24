package org.hotrod.livesql.queries.select.sets;

import org.hotrod.livesql.queries.QueryWriter;

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
