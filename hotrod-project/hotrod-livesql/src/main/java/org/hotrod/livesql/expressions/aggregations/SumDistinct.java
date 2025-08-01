package org.hotrod.livesql.expressions.aggregations;

import org.hotrod.livesql.expressions.numeric.GeneralNumericExpression;
import org.hotrod.livesql.expressions.numeric.NumericFunction;

public class SumDistinct extends NumericFunction implements NonWindowableAggregationFunction {

  public SumDistinct(final GeneralNumericExpression expression) {
    super("sum(distinct #{})", expression);
  }

}
