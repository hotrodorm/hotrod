package org.hotrod.livesql.expressions.aggregations;

import org.hotrod.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.livesql.expressions.numeric.NumericExpression;

public class NumericMax extends NumericAggregationUnfilteredFunction implements WindowableAggregationFunction {

  public NumericMax(final NumericExpression expression) {
    super("max", expression);
  }

}
