package org.hotrod.runtime.livesql.expressions.aggregations;

import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;
import org.hotrod.runtime.livesql.expressions.numbers.NumberFunction;

public class AvgDistinct extends NumberFunction implements NonWindowableAggregationFunction {

  public AvgDistinct(final NumberExpression expression) {
    super("avg", "distinct", expression);
  }

}
