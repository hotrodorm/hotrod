package org.hotrod.runtime.livesql.queries.select.sets;

import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class UnionAllOperator<R> extends SetOperator<R> {

  public UnionAllOperator() {
    super();
  }

  @Override
  protected void renderSetOperator(final QueryWriter w) {
    w.getSQLDialect().getSetOperationRenderer().renderUnionAll(w);
    w.write("UNION ALL");
  }

  @Override
  public int getPrecedence() {
    return PRECEDENCE_UNION_ALL;
  }

}