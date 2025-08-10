package org.hotrod.livesql.expressions.bool;

import org.hotrod.livesql.expressions.Expression;

public class And extends BinaryPredicate {

  public And(final BooleanExpression a, final BooleanExpression b) {
    super(a, "and", b, Expression.PRECEDENCE_AND);
  }

}
