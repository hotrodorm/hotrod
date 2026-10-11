package org.hotrod.livesql.expressions.aggregations;

import org.hotrod.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.livesql.expressions.binary.BinaryExpression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class BinaryFilteredFunction extends BinaryAggregationFunction implements WindowableAggregationFunction {

  protected BinaryFilteredFunction(String name, BinaryExpression expression, Predicate filter) {
    super(name, expression, filter);
  }

}
