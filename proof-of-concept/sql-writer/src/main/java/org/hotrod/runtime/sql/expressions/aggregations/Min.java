package org.hotrod.runtime.sql.expressions.aggregations;

import org.hotrod.runtime.sql.expressions.Expression;

public class Min extends AggregationFunction {

  protected Min(final Expression expression) {
    super("min", null, Expression.toList(expression));
  }

}
