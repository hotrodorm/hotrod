package org.hotrod.runtime.livesql.queries.select.sets;

import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class IntersectAllOperator<R> extends SetOperator<R> {

  public IntersectAllOperator() {
    super();
  }

  @Override
  protected void renderSetOperator(QueryWriter w) {
    w.write("INTERSECT ALL");
  }

  @Override
  public int getPrecedence() {
    return PRECEDENCE_INTERSECT_ALL;
  }

}
