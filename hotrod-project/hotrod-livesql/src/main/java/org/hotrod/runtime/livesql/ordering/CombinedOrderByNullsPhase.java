package org.hotrod.runtime.livesql.ordering;

import org.hotrod.runtime.livesql.expressions.OrderingTerm;
import org.hotrod.runtime.livesql.queries.QueryWriter;

public class CombinedOrderByNullsPhase extends OrderingTerm {

  private OrderByProperties ordering;

  public CombinedOrderByNullsPhase(final OrderByProperties ordering) {
    super(PRECEDENCE_ORDERING);
    this.ordering = ordering;
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    this.ordering.renderTo(w);
  }

}
