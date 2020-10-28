package org.hotrod.runtime.livesql.expressions.strings;

import org.hotrod.runtime.livesql.expressions.Expression;

public abstract class BuiltInStringFunction extends StringExpression {

  protected BuiltInStringFunction() {
    super(Expression.PRECEDENCE_FUNCTION);
  }

}
