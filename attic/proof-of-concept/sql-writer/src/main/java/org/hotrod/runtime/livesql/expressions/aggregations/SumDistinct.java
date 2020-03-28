package org.hotrod.runtime.livesql.expressions.aggregations;

import org.hotrod.runtime.livesql.expressions.Expression;

public class SumDistinct extends NonWindowableAggregationFunction<Number> {

  public SumDistinct(final Expression<?> expression) {
    super("sum", "distinct", expression);
  }

}
