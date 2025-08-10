package org.hotrod.livesql.expressions.character;

import org.hotrod.livesql.expressions.Expression;

public abstract class BuiltInCharFunction extends CharSyntaxExpression {

  protected BuiltInCharFunction() {
    super(Expression.PRECEDENCE_FUNCTION);
  }

}
