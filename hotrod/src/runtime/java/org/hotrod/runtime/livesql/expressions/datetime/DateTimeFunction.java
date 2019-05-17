package org.hotrod.runtime.livesql.expressions.datetime;

import org.hotrod.runtime.livesql.expressions.Expression;

public abstract class DateTimeFunction extends DateTimeExpression {

  protected DateTimeFunction() {
    super(Expression.PRECEDENCE_FUNCTION);
  }

}
