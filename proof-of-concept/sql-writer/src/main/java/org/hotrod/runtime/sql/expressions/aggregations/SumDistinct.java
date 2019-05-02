package org.hotrod.runtime.sql.expressions.aggregations;

import org.hotrod.runtime.sql.expressions.Expression;

public class SumDistinct extends NonWindowableAggregationFunction<Number> {

  public SumDistinct(final Expression<?> expression) {
    super("sum", "distinct", expression);
  }

}
