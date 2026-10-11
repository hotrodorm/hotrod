package org.hotrod.livesql.expressions.aggregations;

import org.hotrod.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.livesql.expressions.datetime.DateTimeExpression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class DateTimeUnfilteredFunction extends DateTimeAggregationFunction implements WindowableAggregationFunction {

  protected DateTimeUnfilteredFunction(final String name, final DateTimeExpression expression) {
    super(name, expression, null);
  }

  public DateTimeFilteredFunction filter(final Predicate filter) {
    return new DateTimeFilteredFunction(super.name, super.expression, filter);
  }

}
