package org.hotrod.runtime.livesql.ordering;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.ordering.OrderByProperties.NullsOrdering;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class OrderByDirectionStage implements OrderingTerm {

  private OrderByProperties ordering;

  public OrderByDirectionStage(final Expression<?> expression, final boolean ascending) {
    this.ordering = new OrderByProperties(expression, ascending);
  }

  public OrderByNullsStage nullsFirst() {
    this.ordering.setNullsOrdering(NullsOrdering.NULLS_FIRST);
    return new OrderByNullsStage(this.ordering);
  }

  public OrderByNullsStage nullsLast() {
    this.ordering.setNullsOrdering(NullsOrdering.NULLS_LAST);
    return new OrderByNullsStage(this.ordering);
  }

  @Override
  public void renderTo(final QueryWriter w) {
    this.ordering.renderTo(w);
  }

}
