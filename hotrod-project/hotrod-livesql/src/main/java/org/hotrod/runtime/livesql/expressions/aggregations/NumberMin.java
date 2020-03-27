package org.hotrod.runtime.livesql.expressions.aggregations;

import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;

public class NumberMin extends NumericAggregationFunction {

  public NumberMin(final NumberExpression expression) {
    super("min", expression);
  }

}
