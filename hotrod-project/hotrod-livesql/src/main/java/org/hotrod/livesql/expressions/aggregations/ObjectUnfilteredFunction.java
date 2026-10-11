package org.hotrod.livesql.expressions.aggregations;

import org.hotrod.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.livesql.expressions.object.ObjectExpression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class ObjectUnfilteredFunction extends ObjectAggregationFunction implements WindowableAggregationFunction {

  protected ObjectUnfilteredFunction(final String name, final ObjectExpression expression) {
    super(name, expression, null);
  }

  public ObjectFilteredFunction filter(final Predicate filter) {
    return new ObjectFilteredFunction(super.name, super.expression, filter);
  }

}
