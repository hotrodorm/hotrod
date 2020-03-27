package org.hotrod.runtime.livesql.expressions.aggregations;

import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class BooleanMin extends BooleanAggregationFunction {

  public BooleanMin(final Predicate expression) {
    super("min", expression);
  }

}
