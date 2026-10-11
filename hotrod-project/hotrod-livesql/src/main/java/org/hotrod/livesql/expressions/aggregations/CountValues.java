package org.hotrod.livesql.expressions.aggregations;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.analytics.WindowableAggregationFunction;

public class CountValues extends NumericAggregationUnfilteredFunction implements WindowableAggregationFunction {

  public CountValues(final ComparableExpression expression) {
    super("count", expression);
  }

}
