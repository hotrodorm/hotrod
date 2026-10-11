package org.hotrod.livesql.expressions.aggregations;

import org.hotrod.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.livesql.expressions.character.CharExpression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class CharUnfilteredFunction extends CharAggregationFunction implements WindowableAggregationFunction {

  protected CharUnfilteredFunction(final String name, final CharExpression expression) {
    super(name, expression, null);
  }

  public CharFilteredFunction filter(final Predicate filter) {
    return new CharFilteredFunction(super.name, super.expression, filter);
  }

}
