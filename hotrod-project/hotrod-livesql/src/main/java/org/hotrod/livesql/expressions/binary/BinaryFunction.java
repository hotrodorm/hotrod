package org.hotrod.livesql.expressions.binary;

import java.util.Arrays;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.Expression;

public abstract class BinaryFunction extends BinarySyntaxExpression {

  protected BinaryFunction(final ComparableExpression... parameters) {
    super(Expression.PRECEDENCE_FUNCTION);
    Arrays.asList(parameters).forEach(p -> super.register(p));
  }

}
