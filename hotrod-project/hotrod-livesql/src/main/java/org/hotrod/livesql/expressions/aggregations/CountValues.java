package org.hotrod.livesql.expressions.aggregations;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.analytics.NumericWindowExpression;
import org.hotrod.livesql.expressions.analytics.NumericWindowFunctionOverStage;
import org.hotrod.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.livesql.expressions.numeric.NumericFunction;

public class CountValues extends NumericFunction implements WindowableAggregationFunction {

  public CountValues(final ComparableExpression parameter) {
    super("count(#{})", parameter);
  }

  public NumericWindowFunctionOverStage over() {
    return new NumericWindowFunctionOverStage(new NumericWindowExpression(this));
  }

}
