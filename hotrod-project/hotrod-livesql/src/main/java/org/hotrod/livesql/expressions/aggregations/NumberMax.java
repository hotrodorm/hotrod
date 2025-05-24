package org.hotrod.livesql.expressions.aggregations;

import org.hotrod.livesql.expressions.analytics.NumberWindowExpression;
import org.hotrod.livesql.expressions.analytics.NumberWindowFunctionOverStage;
import org.hotrod.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.livesql.expressions.numbers.GeneralNumberExpression;
import org.hotrod.livesql.expressions.numbers.NumberFunction;

public class NumberMax extends NumberFunction implements WindowableAggregationFunction {

  public NumberMax(final GeneralNumberExpression expression) {
    super("max(#{})", expression);
  }

  public NumberWindowFunctionOverStage over() {
    return new NumberWindowFunctionOverStage(new NumberWindowExpression(this));
  }

}
