package org.hotrod.runtime.livesql.expressions.datetime;

import org.hotrod.runtime.livesql.expressions.Expression;

public abstract class BuiltInDateTimeFunction extends DateTimeExpression {

  protected BuiltInDateTimeFunction() {
    super(Expression.PRECEDENCE_FUNCTION);
  }

}
