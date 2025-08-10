package org.hotrod.livesql.expressions.bool;

import org.hotrod.livesql.expressions.Expression;

public class Or extends BinaryPredicate {

  public Or(final BooleanExpression a, final BooleanExpression b) {
    super(a, "or", b, Expression.PRECEDENCE_OR);
  }

}
