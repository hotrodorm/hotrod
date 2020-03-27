package org.hotrod.runtime.livesql.expressions.aggregations;

import org.hotrod.runtime.livesql.expressions.Expression;

public class Sum extends NumericAggregationFunction {

  public Sum(final Expression<Number> expression) {
    super("sum", expression);
  }

}
