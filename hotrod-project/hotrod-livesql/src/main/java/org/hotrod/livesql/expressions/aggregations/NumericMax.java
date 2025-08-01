package org.hotrod.livesql.expressions.aggregations;

import org.hotrod.livesql.expressions.analytics.NumericWindowExpression;
import org.hotrod.livesql.expressions.analytics.NumericWindowFunctionOverStage;
import org.hotrod.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.livesql.expressions.numeric.GeneralNumericExpression;
import org.hotrod.livesql.expressions.numeric.NumericFunction;

public class NumericMax extends NumericFunction implements WindowableAggregationFunction {

  public NumericMax(final GeneralNumericExpression expression) {
    super("max(#{})", expression);
  }

  public NumericWindowFunctionOverStage over() {
    return new NumericWindowFunctionOverStage(new NumericWindowExpression(this));
  }

}
