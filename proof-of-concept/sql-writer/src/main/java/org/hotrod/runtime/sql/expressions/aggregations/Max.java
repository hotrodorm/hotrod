package org.hotrod.runtime.sql.expressions.aggregations;

import org.hotrod.runtime.sql.expressions.Expression;

public class Max extends NumericAggregationFunction {

  public Max(final Expression<Number> expression) {
    super("max", expression);
  }

}
