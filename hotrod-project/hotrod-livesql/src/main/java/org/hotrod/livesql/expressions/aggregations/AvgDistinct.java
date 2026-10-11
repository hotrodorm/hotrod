package org.hotrod.livesql.expressions.aggregations;

import org.hotrod.livesql.expressions.numeric.NumericExpression;

public class AvgDistinct extends NumericAggregationUnfilteredFunction implements NonWindowableAggregationFunction {

  public AvgDistinct(final NumericExpression expression) {
    super("avg", expression);
    super.setDistinct();
  }

}
