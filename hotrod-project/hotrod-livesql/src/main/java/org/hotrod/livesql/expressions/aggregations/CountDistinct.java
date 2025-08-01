package org.hotrod.livesql.expressions.aggregations;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.numeric.NumericFunction;

public class CountDistinct extends NumericFunction implements NonWindowableAggregationFunction {

  public CountDistinct(final ComparableExpression... parameters) {
    super("count(distinct #{})", parameters);
  }

}
