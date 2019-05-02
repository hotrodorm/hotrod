package org.hotrod.runtime.sql.expressions.aggregations;

import org.hotrod.runtime.sql.expressions.Expression;

public class Sum extends NumericAggregationFunction {

  public Sum(final Expression<Number> expression) {
    super("sum", expression);
  }

}
