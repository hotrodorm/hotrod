package org.hotrod.livesql.expressions.aggregations;

import org.hotrod.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.livesql.expressions.numeric.NumericExpression;

public class Avg extends NumericAggregationUnfilteredFunction implements WindowableAggregationFunction {

  public Avg(final NumericExpression expression) {
    super("avg", expression);
  }

}
