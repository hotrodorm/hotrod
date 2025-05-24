package org.hotrod.livesql.expressions.strings;

import org.hotrod.livesql.expressions.Expression;

public abstract class BuiltInStringFunction extends StringExpression {

  protected BuiltInStringFunction() {
    super(Expression.PRECEDENCE_FUNCTION);
  }

}
