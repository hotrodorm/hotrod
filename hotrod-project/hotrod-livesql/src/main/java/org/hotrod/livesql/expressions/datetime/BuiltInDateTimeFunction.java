package org.hotrod.livesql.expressions.datetime;

import org.hotrod.livesql.expressions.Expression;

public abstract class BuiltInDateTimeFunction extends DateTimeSyntaxExpression {

  protected BuiltInDateTimeFunction() {
    super(Expression.PRECEDENCE_FUNCTION);
  }

}
