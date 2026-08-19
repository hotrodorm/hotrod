package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.datetime.DateTimeExpression;
import org.hotrod.livesql.expressions.datetime.DateTimeFunction;

public class DateTimeFirstValue extends DateTimeFunction implements PositionalAnalyticFunction {

  public DateTimeFirstValue(final DateTimeExpression expression) {
    super("first_value(#{})", expression);
  }

  public DateTimeWindowFunctionOverStage over() {
    return new DateTimeWindowFunctionOverStage(new DateTimeWindowExpression(this));
  }

}
