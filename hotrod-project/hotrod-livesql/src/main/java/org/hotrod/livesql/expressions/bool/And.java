package org.hotrod.livesql.expressions.bool;

import org.hotrod.livesql.expressions.Expression;

public class And extends BinaryPredicate {

  public And(final GeneralBooleanExpression a, final GeneralBooleanExpression b) {
    super(a, "and", b, Expression.PRECEDENCE_AND);
  }

}
