package org.hotrod.livesql.expressions.aggregations;

import org.hotrod.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.livesql.expressions.object.ObjectExpression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class ObjectFilteredFunction extends ObjectAggregationFunction implements WindowableAggregationFunction {

  protected ObjectFilteredFunction(String name, ObjectExpression expression, Predicate filter) {
    super(name, expression, filter);
  }

}
