package org.hotrod.runtime.sql.expressions.aggregations;

import org.hotrod.runtime.sql.expressions.Expression;

public class AvgDistinct extends NonWindowableAggregationFunction<Number> {

  public AvgDistinct(final Expression<?> expression) {
    super("sum", "distinct", expression);
  }

}
