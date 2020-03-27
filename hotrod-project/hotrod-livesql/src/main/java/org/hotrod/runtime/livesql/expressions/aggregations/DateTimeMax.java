package org.hotrod.runtime.livesql.expressions.aggregations;

import org.hotrod.runtime.livesql.expressions.datetime.DateTimeExpression;

public class DateTimeMax extends DateTimeAggregationFunction {

  public DateTimeMax(final DateTimeExpression expression) {
    super("max", expression);
  }

}
