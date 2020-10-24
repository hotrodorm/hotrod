package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.datetime.DateTimeExpression;
import org.hotrod.runtime.livesql.expressions.datetime.DateTimeFunction;
import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;

public class DateTimeLead extends DateTimeFunction implements PositionalAnalyticFunction {

  public DateTimeLead(final DateTimeExpression expression, final NumberExpression offset,
      final DateTimeExpression defaultValue) {
    super("lead", expression, offset, defaultValue);
  }

}
