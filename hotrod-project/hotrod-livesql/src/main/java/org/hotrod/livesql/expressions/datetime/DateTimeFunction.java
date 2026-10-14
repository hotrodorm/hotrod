package org.hotrod.livesql.expressions.datetime;

import java.util.Arrays;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.Expression;

public abstract class DateTimeFunction extends DateTimeSyntaxExpression {

  protected DateTimeFunction(final ComparableExpression... parameters) {
    super(Expression.PRECEDENCE_FUNCTION);
    Arrays.asList(parameters).forEach(p -> super.register(p));
  }

}
