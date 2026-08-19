package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.datetime.DateTimeExpression;
import org.hotrod.livesql.expressions.datetime.DateTimeFunction;

public class DateTimeLastValue extends DateTimeFunction implements PositionalAnalyticFunction {

  public DateTimeLastValue(final DateTimeExpression expression) {
    super("last_value(#{})", expression);
  }

  public DateTimeWindowFunctionOverStage over() {
    return new DateTimeWindowFunctionOverStage(new DateTimeWindowExpression(this));
  }

}
