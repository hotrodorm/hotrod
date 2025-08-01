package org.hotrod.livesql.expressions.character;

import org.hotrod.livesql.expressions.Expression;

public abstract class BuiltInCharFunction extends CharExpression {

  protected BuiltInCharFunction() {
    super(Expression.PRECEDENCE_FUNCTION);
  }

}
