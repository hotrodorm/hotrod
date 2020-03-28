package org.hotrod.runtime.livesql.expressions.aggregations;

import org.hotrod.runtime.livesql.expressions.Expression;

public class Avg extends NumericAggregationFunction {

  public Avg(final Expression<Number> expression) {
    super("avg", expression);
  }

}
