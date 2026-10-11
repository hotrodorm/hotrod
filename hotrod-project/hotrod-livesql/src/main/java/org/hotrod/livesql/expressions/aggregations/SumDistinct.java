package org.hotrod.livesql.expressions.aggregations;

import org.hotrod.livesql.expressions.numeric.NumericExpression;

public class SumDistinct extends NumericAggregationUnfilteredFunction implements NonWindowableAggregationFunction {

  public SumDistinct(final NumericExpression expression) {
    super("sum", expression);
    super.setDistinct();
  }

}
