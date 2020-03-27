package org.hotrod.runtime.livesql.expressions.aggregations;

import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class BooleanMax extends BooleanAggregationFunction {

  public BooleanMax(final Predicate expression) {
    super("max", expression);
  }

}
