package org.hotrod.runtime.sql.expressions.aggregations;

import org.hotrod.runtime.sql.expressions.Expression;

public class Sum extends AggregationFunction {

  protected Sum(final Expression expression) {
    super("sum", null, Expression.toList(expression));
  }

}
