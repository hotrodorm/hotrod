package org.hotrod.livesql.expressions.aggregations;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class NumericAggregationUnfilteredFunction extends NumericAggregationFunction
    implements WindowableAggregationFunction {

  public NumericAggregationUnfilteredFunction(final String name, ComparableExpression expression) {
    super(name, expression);
  }

  public NumericAggregationFilteredFunction filter(final Predicate filter) {
    return new NumericAggregationFilteredFunction(this, filter);
  }

}
