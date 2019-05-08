package org.hotrod.runtime.livesql.expressions.aggregations;

import org.hotrod.runtime.livesql.expressions.Expression;

public class CountDistinct extends NonWindowableAggregationFunction<Number> {

  public CountDistinct(final Expression<?> expression) {
    super("count", "distinct", expression);
  }

}
