package org.hotrod.livesql.expressions.aggregations;

import org.hotrod.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class NumericAggregationFilteredFunction extends NumericAggregationFunction implements WindowableAggregationFunction {

  public NumericAggregationFilteredFunction(final NumericAggregationFunction function, Predicate filter) {
    super(function, filter);
  }

}
