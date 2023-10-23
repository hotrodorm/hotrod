package org.hotrod.runtime.livesql.queries.select.sets;

import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class IntersectOperator extends SetOperator {

  public IntersectOperator() {
    super();
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.getSQLDialect().getSetOperationRenderer().renderIntersect(w);
  }

  @Override
  public int getPrecedence() {
    return PRECEDENCE_INTERSECT;
  }

}
