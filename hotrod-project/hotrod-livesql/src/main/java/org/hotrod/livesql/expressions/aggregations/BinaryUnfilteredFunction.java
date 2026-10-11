package org.hotrod.livesql.expressions.aggregations;

import org.hotrod.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.livesql.expressions.binary.BinaryExpression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class BinaryUnfilteredFunction extends BinaryAggregationFunction implements WindowableAggregationFunction {

  protected BinaryUnfilteredFunction(final String name, final BinaryExpression expression) {
    super(name, expression, null);
  }

  public BinaryFilteredFunction filter(final Predicate filter) {
    return new BinaryFilteredFunction(super.name, super.expression, filter);
  }

}
