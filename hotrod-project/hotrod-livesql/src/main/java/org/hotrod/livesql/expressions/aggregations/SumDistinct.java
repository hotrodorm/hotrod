package org.hotrod.livesql.expressions.aggregations;

import org.hotrod.livesql.expressions.numbers.GeneralNumberExpression;
import org.hotrod.livesql.expressions.numbers.NumberFunction;

public class SumDistinct extends NumberFunction implements NonWindowableAggregationFunction {

  public SumDistinct(final GeneralNumberExpression expression) {
    super("sum(distinct #{})", expression);
  }

}
