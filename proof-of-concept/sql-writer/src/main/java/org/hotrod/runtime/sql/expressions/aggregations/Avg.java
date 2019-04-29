package org.hotrod.runtime.sql.expressions.aggregations;

import org.hotrod.runtime.sql.expressions.Expression;

public class Avg extends NumericAggregationFunction {

  protected Avg(final Expression<Number> expression) {
    super("avg", null, NumericAggregationFunction.toList(expression));
  }

}
