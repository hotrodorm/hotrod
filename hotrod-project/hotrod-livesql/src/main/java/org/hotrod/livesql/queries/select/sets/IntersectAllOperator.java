package org.hotrod.livesql.queries.select.sets;

import org.hotrod.livesql.queries.QueryWriter;

public class IntersectAllOperator extends SetOperator {

  public IntersectAllOperator() {
    super();
  }

  @Override
  public boolean isUnion() {
    return false;
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
