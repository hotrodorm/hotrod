package org.hotrod.runtime.livesql.expressions.strings;

import org.hotrod.runtime.livesql.expressions.Expression;

public abstract class StringFunction extends StringExpression {

  protected StringFunction() {
    super(Expression.PRECEDENCE_FUNCTION);
  }

}
