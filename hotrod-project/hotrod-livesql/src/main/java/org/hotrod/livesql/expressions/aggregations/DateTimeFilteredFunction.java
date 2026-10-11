package org.hotrod.livesql.expressions.aggregations;

import org.hotrod.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.livesql.expressions.datetime.DateTimeExpression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class DateTimeFilteredFunction extends DateTimeAggregationFunction implements WindowableAggregationFunction {

  protected DateTimeFilteredFunction(String name, DateTimeExpression expression, Predicate filter) {
    super(name, expression, filter);
  }

}
