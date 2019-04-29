package org.hotrod.runtime.sql.expressions.aggregations;

import org.hotrod.runtime.sql.expressions.Expression;

public class Max extends AggregationFunction {

  protected Max(final Expression expression) {
    super("max", null, Expression.toList(expression));
  }

}
