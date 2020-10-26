package org.hotrod.runtime.livesql.expressions.aggregations;

import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;
import org.hotrod.runtime.livesql.expressions.numbers.NumberFunction;

public class SumDistinct extends NumberFunction implements NonWindowableAggregationFunction {

  public SumDistinct(final NumberExpression expression) {
    super("sum(distinct #{})", expression);
  }

}
