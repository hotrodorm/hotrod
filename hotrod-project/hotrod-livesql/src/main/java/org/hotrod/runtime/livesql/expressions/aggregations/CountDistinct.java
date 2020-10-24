package org.hotrod.runtime.livesql.expressions.aggregations;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.numbers.NumberFunction;

public class CountDistinct extends NumberFunction implements NonWindowableAggregationFunction {

  public CountDistinct(final Expression... parameters) {
    super("count", "distinct", parameters);
  }

}
