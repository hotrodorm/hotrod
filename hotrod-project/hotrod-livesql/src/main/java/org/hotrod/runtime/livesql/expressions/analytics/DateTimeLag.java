package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.datetime.DateTimeFunction;
import org.hotrod.runtime.livesql.expressions.datetime.GeneralDateTimeExpression;
import org.hotrod.runtime.livesql.expressions.numbers.GeneralNumberExpression;

public class DateTimeLag extends DateTimeFunction implements PositionalAnalyticFunction {

  public DateTimeLag(final GeneralDateTimeExpression expression) {
    super("lag(#{})", expression);
  }

  public DateTimeLag(final GeneralDateTimeExpression expression, final GeneralNumberExpression offset) {
    super("lag(#{}, #{})", expression, offset);
  }

  public DateTimeLag(final GeneralDateTimeExpression expression, final GeneralNumberExpression offset,
      final GeneralDateTimeExpression defaultValue) {
    super("lag(#{}, #{}, #{})", expression, offset, defaultValue);
  }

  public DateTimeWindowFunctionOverStage over() {
    return new DateTimeWindowFunctionOverStage(new DateTimeWindowExpression(this));
  }

}
