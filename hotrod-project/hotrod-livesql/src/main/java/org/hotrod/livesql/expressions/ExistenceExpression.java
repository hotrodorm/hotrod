package org.hotrod.livesql.expressions;

import org.hotrod.livesql.expressions.predicates.IsNotNull;
import org.hotrod.livesql.expressions.predicates.IsNull;
import org.hotrod.livesql.expressions.predicates.Predicate;
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
