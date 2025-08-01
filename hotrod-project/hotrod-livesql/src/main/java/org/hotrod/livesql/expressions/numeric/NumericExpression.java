package org.hotrod.livesql.expressions.numeric;

import org.hotrod.livesql.expressions.TypedExpression;

public abstract class NumericExpression extends GeneralNumericExpression {

  protected NumericExpression(int precedence) {
    super(precedence);
  }

  // TypeHandler setter

  public TypedExpression type(final Class<?> type) {
    return new TypedExpression(this, type);
  }

}
