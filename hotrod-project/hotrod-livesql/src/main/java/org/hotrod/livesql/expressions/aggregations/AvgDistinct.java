package org.hotrod.livesql.expressions.aggregations;

import org.hotrod.livesql.expressions.numeric.NumericExpression;
import org.hotrod.livesql.expressions.numeric.NumericFunction;

public class AvgDistinct extends NumericFunction implements NonWindowableAggregationFunction {

  public AvgDistinct(final NumericExpression expression) {
    super("avg(distinct #{})", expression);
  }

}
