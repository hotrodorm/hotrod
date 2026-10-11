package org.hotrod.livesql.expressions.aggregations;

import org.hotrod.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.livesql.expressions.numeric.NumericExpression;

public class NumericMin extends NumericAggregationUnfilteredFunction implements WindowableAggregationFunction {

  public NumericMin(final NumericExpression expression) {
    super("min", expression);
  }

}
