package org.hotrod.livesql.expressions.aggregations;

import org.hotrod.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class BooleanUnfilteredFunction extends BooleanAggregationFunction implements WindowableAggregationFunction {

  protected BooleanUnfilteredFunction(final String name, final Predicate expression) {
    super(name, expression, null);
  }

  public BooleanFilteredFunction filter(final Predicate filter) {
    return new BooleanFilteredFunction(super.name, super.expression, filter);
  }

}
