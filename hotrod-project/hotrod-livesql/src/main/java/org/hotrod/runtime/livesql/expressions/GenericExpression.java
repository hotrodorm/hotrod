package org.hotrod.runtime.livesql.expressions;

import org.hotrod.runtime.livesql.expressions.predicates.IsNotNull;
import org.hotrod.runtime.livesql.expressions.predicates.IsNull;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public abstract class GenericExpression extends Expression {

  protected GenericExpression(int precedence) {
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
