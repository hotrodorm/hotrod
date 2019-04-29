package org.hotrod.runtime.sql.expressions.aggregations;

import org.hotrod.runtime.sql.expressions.Expression;

public class Min extends NumericAggregationFunction {

  protected Min(final Expression<Number> expression) {
    super("min", null, NumericAggregationFunction.toList(expression));
  }

}
