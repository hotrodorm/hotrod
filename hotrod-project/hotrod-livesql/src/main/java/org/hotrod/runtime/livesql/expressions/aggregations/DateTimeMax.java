package org.hotrod.runtime.livesql.expressions.aggregations;

import org.hotrod.runtime.livesql.expressions.analytics.DateTimeWindowExpression;
import org.hotrod.runtime.livesql.expressions.analytics.DateTimeWindowFunctionOverStage;
import org.hotrod.runtime.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.runtime.livesql.expressions.datetime.DateTimeExpression;
import org.hotrod.runtime.livesql.expressions.datetime.DateTimeFunction;

public class DateTimeMax extends DateTimeFunction implements WindowableAggregationFunction {

  public DateTimeMax(final DateTimeExpression expression) {
    super("max(#{})", expression);
  }

  public DateTimeWindowFunctionOverStage over() {
    return new DateTimeWindowFunctionOverStage(new DateTimeWindowExpression(this));
  }

}
