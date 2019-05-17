package org.hotrod.runtime.livesql.ordering;

import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class OrderByNullsStage implements OrderingTerm {

  private OrderByProperties ordering;

  public OrderByNullsStage(final OrderByProperties ordering) {
    this.ordering = ordering;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    this.ordering.renderTo(w);
  }

}
