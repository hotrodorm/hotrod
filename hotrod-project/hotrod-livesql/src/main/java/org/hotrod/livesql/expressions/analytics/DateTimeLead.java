package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.datetime.DateTimeFunction;
import org.hotrod.livesql.expressions.datetime.GeneralDateTimeExpression;
import org.hotrod.livesql.expressions.numeric.GeneralNumericExpression;

public class DateTimeLead extends DateTimeFunction implements PositionalAnalyticFunction {

  public DateTimeLead(final GeneralDateTimeExpression expression) {
    super("lead(#{})", expression);
  }

  public DateTimeLead(final GeneralDateTimeExpression expression, final GeneralNumericExpression offset) {
    super("lead(#{}, #{})", expression, offset);
  }

  public DateTimeLead(final GeneralDateTimeExpression expression, final GeneralNumericExpression offset,
      final GeneralDateTimeExpression defaultValue) {
    super("lead(#{}, #{}, #{})", expression, offset, defaultValue);
  }

  public DateTimeWindowFunctionOverStage over() {
    return new DateTimeWindowFunctionOverStage(new DateTimeWindowExpression(this));
  }

}
