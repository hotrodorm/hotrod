package org.hotrod.livesql.ordering;

import org.hotrod.livesql.queries.QueryWriter;

public class CombinedOrderByNullsPhase extends CombinedOrderingTerm {

  private OrderByProperties ordering;

  public CombinedOrderByNullsPhase(final OrderByProperties ordering) {
    this.ordering = ordering;
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    this.ordering.renderTo(w);
  }

}
