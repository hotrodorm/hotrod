package org.hotrod.runtime.livesql.expressions.aggregations;

import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;

public class NumberMax extends NumericAggregationFunction {

  public NumberMax(final NumberExpression expression) {
    super("max", expression);
  }

}
