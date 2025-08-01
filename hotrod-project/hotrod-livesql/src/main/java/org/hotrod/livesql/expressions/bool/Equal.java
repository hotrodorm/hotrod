package org.hotrod.livesql.expressions.bool;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.Expression;

public class Equal extends BinaryPredicate {

  public <T extends ComparableExpression> Equal(final T a, final T b) {
    super(a, "=", b, Expression.PRECEDENCE_EQ_NE_LT_LE_GT_GE);
  }

}
