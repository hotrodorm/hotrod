package org.hotrod.livesql.expressions.aggregations;

import org.hotrod.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class BooleanFilteredFunction extends BooleanAggregationFunction implements WindowableAggregationFunction {

  protected BooleanFilteredFunction(String name, Predicate expression, Predicate filter) {
    super(name, expression, filter);
  }

}
