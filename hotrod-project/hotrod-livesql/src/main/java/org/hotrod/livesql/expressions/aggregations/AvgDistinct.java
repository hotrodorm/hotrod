package org.hotrod.livesql.expressions.aggregations;

import org.hotrod.livesql.expressions.numbers.GeneralNumberExpression;
import org.hotrod.livesql.expressions.numbers.NumberFunction;

public class AvgDistinct extends NumberFunction implements NonWindowableAggregationFunction {

  public AvgDistinct(final GeneralNumberExpression expression) {
    super("avg(distinct #{})", expression);
  }

}
