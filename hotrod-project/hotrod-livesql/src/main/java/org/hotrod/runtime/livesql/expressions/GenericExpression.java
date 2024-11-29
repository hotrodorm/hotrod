package org.hotrod.runtime.livesql.expressions;

import org.hotrod.runtime.livesql.expressions.predicates.BooleanFreeExpression;
import org.hotrod.runtime.livesql.expressions.predicates.IsNotNull;
import org.hotrod.runtime.livesql.expressions.predicates.IsNull;
import org.hotrod.runtime.livesql.ordering.OrderingTerm;

public abstract class GenericExpression extends Expression implements OrderingTerm {

  protected GenericExpression(int precedence) {
    super(precedence);
  }

  // Is Null and Is Not Null

  public BooleanFreeExpression isNotNull() {
    return new IsNotNull(this);
  }

  public BooleanFreeExpression isNull() {
    return new IsNull(this);
  }

}
