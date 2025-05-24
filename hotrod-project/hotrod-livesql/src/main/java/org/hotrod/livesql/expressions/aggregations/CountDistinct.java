package org.hotrod.livesql.expressions.aggregations;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.numbers.NumberFunction;

public class CountDistinct extends NumberFunction implements NonWindowableAggregationFunction {

  public CountDistinct(final ComparableExpression... parameters) {
    super("count(distinct #{})", parameters);
  }

}
