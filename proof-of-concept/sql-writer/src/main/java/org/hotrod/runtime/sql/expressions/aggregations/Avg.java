package org.hotrod.runtime.sql.expressions.aggregations;

import org.hotrod.runtime.sql.expressions.Expression;

public class Avg extends NumericAggregationFunction {

  public Avg(final Expression<Number> expression) {
    super("avg", expression);
  }

}
