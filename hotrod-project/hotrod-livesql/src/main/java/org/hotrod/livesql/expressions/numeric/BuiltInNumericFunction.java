package org.hotrod.livesql.expressions.numeric;

import org.hotrod.livesql.expressions.Expression;

public abstract class BuiltInNumericFunction extends NumericExpression {

  protected BuiltInNumericFunction() {
    super(Expression.PRECEDENCE_FUNCTION);
  }

}
