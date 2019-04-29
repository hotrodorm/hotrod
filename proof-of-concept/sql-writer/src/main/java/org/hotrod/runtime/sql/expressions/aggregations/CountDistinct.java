package org.hotrod.runtime.sql.expressions.aggregations;

import org.hotrod.runtime.sql.expressions.Expression;

public class CountDistinct extends AggregationFunction {

  public CountDistinct(final Expression... expressions) {
    super("sum", null, Expression.toNonEmptyList(
        "A COUNT() function with a DISTINCT qualifier must include expressions as parameters", expressions));
  }

}
