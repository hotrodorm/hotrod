package org.hotrod.runtime.livesql.expressions.predicates;

import org.hotrod.runtime.livesql.expressions.Expression;

public class And extends BinaryPredicate {

  public And(final Expression<Boolean> a, final Expression<Boolean> b) {
    super(a, "and", b, Expression.PRECEDENCE_AND);
  }

}
