package org.hotrod.livesql.expressions.aggregations;

import org.hotrod.livesql.expressions.ComparableExpression;

public class CountValuesDistinct extends NumericAggregationUnfilteredFunction
    implements NonWindowableAggregationFunction {

  public CountValuesDistinct(final ComparableExpression parameters) {
    super("count", parameters);
    super.setDistinct();
  }

}
