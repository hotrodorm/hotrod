package org.hotrod.livesql.expressions;

import org.hotrod.livesql.expressions.bool.IsNotNull;
import org.hotrod.livesql.expressions.bool.IsNull;
import org.hotrod.livesql.expressions.bool.Predicate;
import org.hotrod.livesql.ordering.OrderingTerm;

public abstract class ExistenceExpression extends UnaliasedExpression implements OrderingTerm {

  protected ExistenceExpression(int precedence) {
    super(precedence);
  }

  // Is Null and Is Not Null

  public Predicate isNotNull() {
    return new IsNotNull(this);
  }

  public Predicate isNull() {
    return new IsNull(this);
  }

}
