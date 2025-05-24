package org.hotrod.livesql.expressions.aggregations;

import org.hotrod.livesql.expressions.analytics.DateTimeWindowExpression;
import org.hotrod.livesql.expressions.analytics.DateTimeWindowFunctionOverStage;
import org.hotrod.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.livesql.expressions.datetime.DateTimeFunction;
import org.hotrod.livesql.expressions.datetime.GeneralDateTimeExpression;

public class DateTimeMin extends DateTimeFunction implements WindowableAggregationFunction {

  public DateTimeMin(final GeneralDateTimeExpression expression) {
    super("min(#{})", expression);
  }

  public DateTimeWindowFunctionOverStage over() {
    return new DateTimeWindowFunctionOverStage(new DateTimeWindowExpression(this));
  }

}
