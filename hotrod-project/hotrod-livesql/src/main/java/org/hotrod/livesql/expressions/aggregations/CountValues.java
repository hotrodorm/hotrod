package org.hotrod.livesql.expressions.aggregations;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.analytics.NumberWindowExpression;
import org.hotrod.livesql.expressions.analytics.NumberWindowFunctionOverStage;
import org.hotrod.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.livesql.expressions.numbers.NumberFunction;

public class CountValues extends NumberFunction implements WindowableAggregationFunction {

  public CountValues(final ComparableExpression parameter) {
    super("count(#{})", parameter);
  }

  public NumberWindowFunctionOverStage over() {
    return new NumberWindowFunctionOverStage(new NumberWindowExpression(this));
  }

}
