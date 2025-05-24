package org.hotrod.livesql.expressions;

import org.hotrod.livesql.ordering.OrderByDirectionPhase;

public abstract class SortableExpression extends ExistenceExpression {

  // Constructor

  protected SortableExpression(final int precedence) {
    super(precedence);
  }

  // Column ordering

  public final OrderByDirectionPhase asc() {
    return new OrderByDirectionPhase(this, true);
  }

  public final OrderByDirectionPhase desc() {
    return new OrderByDirectionPhase(this, false);
  }

}
