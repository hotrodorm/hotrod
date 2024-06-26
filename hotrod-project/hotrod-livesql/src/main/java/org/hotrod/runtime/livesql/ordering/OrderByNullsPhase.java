package org.hotrod.runtime.livesql.ordering;

import org.hotrod.runtime.livesql.queries.QueryWriter;

public class OrderByNullsPhase extends OrderingExpression {

  private OrderByProperties ordering;

  public OrderByNullsPhase(final OrderByProperties ordering) {
    this.ordering = ordering;
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    this.ordering.renderTo(w);
  }

}
