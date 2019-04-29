package org.hotrod.runtime.sql.expressions.aggregations;

import org.hotrod.runtime.sql.expressions.Expression;

public class CountDistinct extends NumericAggregationFunction {

  public CountDistinct(final Expression<?>... expressions) {
    super("count", "distinct",
        NumericAggregationFunction.toNonEmptyList(
            "A COUNT() function with a DISTINCT qualifier must include at least one expression as parameters",
            expressions));
  }

}
