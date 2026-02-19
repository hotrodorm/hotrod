package org.hotrod.livesql.queries.select.sets;

import org.hotrod.livesql.queries.QueryWriter;

public class IntersectOperator extends SetOperator {

  public IntersectOperator() {
    super();
  }

  @Override
  public boolean isUnion() {
    return false;
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
