package org.hotrod.runtime.livesql.expressions.predicates;

import org.hotrod.runtime.livesql.expressions.ComparableExpression;
import org.hotrod.runtime.livesql.expressions.Expression;

public class LessThanOrEqualTo extends BinaryPredicate {

  public <T extends ComparableExpression> LessThanOrEqualTo(final T a, final T b) {
    super(a, "<=", b, Expression.PRECEDENCE_EQ_NE_LT_LE_GT_GE);
  }

}
