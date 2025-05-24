package org.hotrod.livesql.expressions.aggregations;

import org.hotrod.livesql.expressions.analytics.NumberWindowExpression;
import org.hotrod.livesql.expressions.analytics.NumberWindowFunctionOverStage;
import org.hotrod.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.livesql.expressions.numbers.NumberFunction;

public class CountRows extends NumberFunction implements WindowableAggregationFunction {

  public CountRows() {
    super("count(*)");
  }

  public NumberWindowFunctionOverStage over() {
    return new NumberWindowFunctionOverStage(new NumberWindowExpression(this));
  }

}
