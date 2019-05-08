package org.hotrod.runtime.livesql.expressions.aggregations;

import org.hotrod.runtime.livesql.expressions.Expression;

public class AvgDistinct extends NonWindowableAggregationFunction<Number> {

  public AvgDistinct(final Expression<?> expression) {
    super("sum", "distinct", expression);
  }

}
