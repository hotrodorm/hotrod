package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.datetime.DateTimeFunction;
import org.hotrod.runtime.livesql.expressions.datetime.GeneralDateTimeExpression;
import org.hotrod.runtime.livesql.expressions.numbers.GeneralNumberExpression;

public class DateTimeLead extends DateTimeFunction implements PositionalAnalyticFunction {

  public DateTimeLead(final GeneralDateTimeExpression expression) {
    super("lead(#{})", expression);
  }

  public DateTimeLead(final GeneralDateTimeExpression expression, final GeneralNumberExpression offset) {
    super("lead(#{}, #{})", expression, offset);
  }

  public DateTimeLead(final GeneralDateTimeExpression expression, final GeneralNumberExpression offset,
      final GeneralDateTimeExpression defaultValue) {
    super("lead(#{}, #{}, #{})", expression, offset, defaultValue);
  }

  public DateTimeWindowFunctionOverStage over() {
    return new DateTimeWindowFunctionOverStage(new DateTimeWindowExpression(this));
  }

}
