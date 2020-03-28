package org.hotrod.runtime.livesql.expressions.aggregations;

import org.hotrod.runtime.livesql.expressions.Expression;

public class Min extends NumericAggregationFunction {

  public Min(final Expression<Number> expression) {
    super("min", expression);
  }

}
