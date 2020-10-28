package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.datetime.DateTimeExpression;
import org.hotrod.runtime.livesql.expressions.datetime.DateTimeFunction;
import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;

public class DateTimeLag extends DateTimeFunction implements PositionalAnalyticFunction {

  public DateTimeLag(final DateTimeExpression expression) {
    super("lag(#{})", expression);
  }

  public DateTimeLag(final DateTimeExpression expression, final NumberExpression offset) {
    super("lag(#{}, #{})", expression, offset);
  }

  public DateTimeLag(final DateTimeExpression expression, final NumberExpression offset,
      final DateTimeExpression defaultValue) {
    super("lag(#{}, #{}, #{})", expression, offset, defaultValue);
  }

  public DateTimeWindowFunctionOverStage over() {
    return new DateTimeWindowFunctionOverStage(new DateTimeWindowExpression(this));
  }

}
