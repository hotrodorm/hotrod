package org.hotrod.livesql.expressions.bool;

import java.util.Arrays;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.Expression;

public abstract class BooleanFunction extends BooleanSyntaxExpression {

  protected BooleanFunction(final ComparableExpression... parameters) {
    super(Expression.PRECEDENCE_FUNCTION);
    Arrays.asList(parameters).forEach(p -> super.register(p));
  }

}
