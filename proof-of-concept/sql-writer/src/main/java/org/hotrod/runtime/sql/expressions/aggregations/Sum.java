package org.hotrod.runtime.sql.expressions.aggregations;

import org.hotrod.runtime.sql.expressions.Expression;

public class Sum extends NumericAggregationFunction {

  protected Sum(final Expression<Number> expression) {
    super("sum", null, NumericAggregationFunction.toList(expression));
  }

}
