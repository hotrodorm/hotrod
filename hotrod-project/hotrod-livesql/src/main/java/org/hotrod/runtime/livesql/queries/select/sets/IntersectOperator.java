package org.hotrod.runtime.livesql.queries.select.sets;

import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class IntersectOperator<R> extends SetOperator<R> {

  public IntersectOperator() {
    super();
  }

  @Override
  protected void renderSetOperator(QueryWriter w) {
    w.write("INTERSECT");
  }

  @Override
  public int getPrecedence() {
    return PRECEDENCE_INTERSECT;
  }

}