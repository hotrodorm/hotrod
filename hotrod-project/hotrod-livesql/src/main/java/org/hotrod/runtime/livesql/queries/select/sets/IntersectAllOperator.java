package org.hotrod.runtime.livesql.queries.select.sets;

import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class IntersectAllOperator<R> extends SetOperator<R> {

  public IntersectAllOperator() {
    super();
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.getSQLDialect().getSetOperationRenderer().renderIntersectAll(w);
  }

  @Override
  public int getPrecedence() {
    return PRECEDENCE_INTERSECT_ALL;
  }

}
