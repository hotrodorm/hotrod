package org.hotrod.runtime.livesql.expressions.aggregations;

import org.hotrod.runtime.livesql.expressions.analytics.DateTimeWindowFunctionOverStage;
import org.hotrod.runtime.livesql.expressions.analytics.WindowExpression;
import org.hotrod.runtime.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.runtime.livesql.expressions.datetime.DateTimeExpression;
import org.hotrod.runtime.livesql.expressions.datetime.DateTimeFunction;

public class DateTimeMin extends DateTimeFunction implements WindowableAggregationFunction {

  public DateTimeMin(final DateTimeExpression expression) {
    super("min", expression);
  }

  public DateTimeWindowFunctionOverStage over() {
    return new DateTimeWindowFunctionOverStage(new WindowExpression(this));
  }

}
