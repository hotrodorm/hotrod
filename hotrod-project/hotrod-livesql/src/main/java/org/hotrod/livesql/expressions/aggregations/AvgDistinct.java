package org.hotrod.livesql.expressions.aggregations;

import org.hotrod.livesql.expressions.numeric.GeneralNumericExpression;
import org.hotrod.livesql.expressions.numeric.NumericFunction;

public class AvgDistinct extends NumericFunction implements NonWindowableAggregationFunction {

  public AvgDistinct(final GeneralNumericExpression expression) {
    super("avg(distinct #{})", expression);
  }

}
