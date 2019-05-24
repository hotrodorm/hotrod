package org.hotrod.runtime.livesql.expressions.aggregations;

import org.hotrod.runtime.livesql.expressions.datetime.DateTimeExpression;

public class DateTimeMin extends DateTimeAggregationFunction {

  public DateTimeMin(final DateTimeExpression expression) {
    super("min", expression);
  }

}
