package org.hotrod.runtime.livesql.expressions;

import org.hotrod.runtime.livesql.expressions.predicates.IsNotNull;
import org.hotrod.runtime.livesql.expressions.predicates.IsNull;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.ordering.OrderingTerm;

public abstract class ExistenceExpression extends Expression implements OrderingTerm {

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
