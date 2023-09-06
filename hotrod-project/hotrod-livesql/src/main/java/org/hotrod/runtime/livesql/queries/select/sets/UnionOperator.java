package org.hotrod.runtime.livesql.queries.select.sets;

import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class UnionOperator<R> extends SetOperator<R> {

  public UnionOperator() {
    super();
  }

  @Override
  protected void renderSetOperator(QueryWriter w) {
    w.write("UNION");
  }

  @Override
  public int getPrecedence() {
    return PRECEDENCE_UNION;
  }

}
