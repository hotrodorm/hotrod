package org.hotrod.livesql.expressions.numeric;

import org.hotrod.livesql.expressions.Expression;

public abstract class BuiltInNumericFunction extends NumericSyntaxExpression {

  protected BuiltInNumericFunction() {
    super(Expression.PRECEDENCE_FUNCTION);
  }

}
