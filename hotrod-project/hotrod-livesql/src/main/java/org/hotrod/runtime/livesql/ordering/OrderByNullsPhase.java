package org.hotrod.runtime.livesql.ordering;

import org.hotrod.runtime.livesql.queries.QueryWriter;

public class OrderByNullsPhase implements OrderingTerm {

  private OrderByProperties ordering;

  public OrderByNullsPhase(final OrderByProperties ordering) {
    this.ordering = ordering;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    this.ordering.renderTo(w);
  }

}
