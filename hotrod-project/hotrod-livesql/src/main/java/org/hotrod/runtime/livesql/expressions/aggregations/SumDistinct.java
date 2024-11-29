package org.hotrod.runtime.livesql.expressions.aggregations;

import org.hotrod.runtime.livesql.expressions.numbers.GeneralNumberExpression;
import org.hotrod.runtime.livesql.expressions.numbers.NumberFunction;

public class SumDistinct extends NumberFunction implements NonWindowableAggregationFunction {

  public SumDistinct(final GeneralNumberExpression expression) {
    super("sum(distinct #{})", expression);
  }

}
