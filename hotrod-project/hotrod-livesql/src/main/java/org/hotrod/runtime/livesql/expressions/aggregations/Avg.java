package org.hotrod.runtime.livesql.expressions.aggregations;

import org.hotrod.runtime.livesql.expressions.analytics.NumberWindowExpression;
import org.hotrod.runtime.livesql.expressions.analytics.NumberWindowFunctionOverStage;
import org.hotrod.runtime.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;
import org.hotrod.runtime.livesql.expressions.numbers.NumberFunction;

public class Avg extends NumberFunction implements WindowableAggregationFunction {

  public Avg(final NumberExpression expression) {
    super("avg(#{})", expression);
  }

  public NumberWindowFunctionOverStage over() {
    return new NumberWindowFunctionOverStage(new NumberWindowExpression(this));
  }

}
