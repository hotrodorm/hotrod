package org.hotrod.livesql.ordering;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.ordering.OrderByProperties.NullsOrdering;
import org.hotrod.livesql.queries.QueryWriter;

public class CombinedOrderByDirectionPhase extends CombinedOrderingTerm {

  private OrderByProperties ordering;

  public CombinedOrderByDirectionPhase(final ComparableExpression expression, final boolean ascending) {
    super();
    this.ordering = new OrderByProperties(expression, ascending);
  }

  public CombinedOrderByDirectionPhase(final String alias, final boolean ascending) {
    super();
    this.ordering = new OrderByProperties(alias, ascending);
  }

  public CombinedOrderByDirectionPhase(final int ordinal, final boolean ascending) {
    super();
    this.ordering = new OrderByProperties(ordinal, ascending);
  }

  public CombinedOrderByNullsPhase nullsFirst() {
    this.ordering.setNullsOrdering(NullsOrdering.NULLS_FIRST);
    return new CombinedOrderByNullsPhase(this.ordering);
  }

  public CombinedOrderByNullsPhase nullsLast() {
    this.ordering.setNullsOrdering(NullsOrdering.NULLS_LAST);
    return new CombinedOrderByNullsPhase(this.ordering);
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    this.ordering.renderTo(w);
  }

}
