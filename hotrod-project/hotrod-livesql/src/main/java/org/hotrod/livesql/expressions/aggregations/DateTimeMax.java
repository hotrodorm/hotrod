package org.hotrod.livesql.expressions.aggregations;

import org.hotrod.livesql.expressions.analytics.DateTimeWindowExpression;
import org.hotrod.livesql.expressions.analytics.DateTimeWindowFunctionOverStage;
import org.hotrod.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.livesql.expressions.datetime.DateTimeExpression;

public class DateTimeMax extends DateTimeUnfilteredFunction implements WindowableAggregationFunction {

  public DateTimeMax(final DateTimeExpression expression) {
    super("max", expression);
  }

  public DateTimeWindowFunctionOverStage over() {
    return new DateTimeWindowFunctionOverStage(new DateTimeWindowExpression(this));
  }

}
