package org.hotrod.runtime.livesql.ordering;

import org.hotrod.runtime.livesql.expressions.ComparableExpression;
import org.hotrod.runtime.livesql.ordering.OrderByProperties.NullsOrdering;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class OrderByDirectionPhase implements OrderingTerm {

  private OrderByProperties ordering;

  public OrderByDirectionPhase(final ComparableExpression expression, final boolean ascending) {
    this.ordering = new OrderByProperties(expression, ascending);
  }

  public OrderByDirectionPhase(final String alias, final boolean ascending) {
    this.ordering = new OrderByProperties(alias, ascending);
  }

  public OrderByDirectionPhase(final int ordinal, final boolean ascending) {
    this.ordering = new OrderByProperties(ordinal, ascending);
  }

  public OrderByNullsPhase nullsFirst() {
    this.ordering.setNullsOrdering(NullsOrdering.NULLS_FIRST);
    return new OrderByNullsPhase(this.ordering);
  }

  public OrderByNullsPhase nullsLast() {
    this.ordering.setNullsOrdering(NullsOrdering.NULLS_LAST);
    return new OrderByNullsPhase(this.ordering);
  }

  @Override
  public void renderTo(final QueryWriter w) {
    this.ordering.renderTo(w);
  }

}
