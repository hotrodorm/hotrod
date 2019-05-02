package org.hotrod.runtime.sql.expressions.aggregations;

import org.hotrod.runtime.sql.expressions.Expression;

public class CountDistinct extends NonWindowableAggregationFunction<Number> {

  public CountDistinct(final Expression<?> expression) {
    super("count", "distinct", expression);
  }

}
