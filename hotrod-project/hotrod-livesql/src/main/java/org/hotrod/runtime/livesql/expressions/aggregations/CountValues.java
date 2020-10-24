package org.hotrod.runtime.livesql.expressions.aggregations;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.analytics.NumberWindowFunctionOverStage;
import org.hotrod.runtime.livesql.expressions.analytics.WindowExpression;
import org.hotrod.runtime.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.runtime.livesql.expressions.numbers.NumberFunction;

public class CountValues extends NumberFunction implements WindowableAggregationFunction {

  public CountValues(final Expression parameter) {
    super("count", "", parameter);
  }

  public NumberWindowFunctionOverStage over() {
    return new NumberWindowFunctionOverStage(new WindowExpression(this));
  }

}
