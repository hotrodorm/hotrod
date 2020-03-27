package org.hotrod.runtime.livesql.expressions.predicates;

import org.hotrod.runtime.livesql.expressions.Expression;

public class GreaterThan extends BinaryPredicate {

  public <T> GreaterThan(final Expression<T> a, final Expression<T> b) {
    super(a, ">", b, Expression.PRECEDENCE_EQ_NE_LT_LE_GT_GE);
  }

}
