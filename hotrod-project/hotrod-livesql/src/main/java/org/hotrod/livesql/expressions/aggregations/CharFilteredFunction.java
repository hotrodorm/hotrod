package org.hotrod.livesql.expressions.aggregations;

import org.hotrod.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.livesql.expressions.character.CharExpression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class CharFilteredFunction extends CharAggregationFunction implements WindowableAggregationFunction {

  protected CharFilteredFunction(String name, CharExpression expression, Predicate filter) {
    super(name, expression, filter);
  }

}
