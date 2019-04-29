package org.hotrod.runtime.sql.expressions.aggregations;

import org.hotrod.runtime.sql.expressions.Expression;

public class Avg extends AggregationFunction {

  protected Avg(final Expression expression) {
    super("avg", null, Expression.toList(expression));
  }

}
