package org.hotrod.livesql.expressions.aggregations;

import org.hotrod.livesql.expressions.numeric.NumericExpression;
import org.hotrod.livesql.expressions.numeric.NumericFunction;

public class SumDistinct extends NumericFunction implements NonWindowableAggregationFunction {

  public SumDistinct(final NumericExpression expression) {
    super("sum(distinct #{})", expression);
  }

}
