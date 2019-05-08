package org.hotrod.runtime.livesql.expressions.aggregations;

import org.hotrod.runtime.livesql.expressions.Expression;

public class Max extends NumericAggregationFunction {

  public Max(final Expression<Number> expression) {
    super("max", expression);
  }

}
