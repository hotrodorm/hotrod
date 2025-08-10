package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.datetime.DateTimeFunction;
import org.hotrod.livesql.expressions.datetime.DateTimeExpression;
import org.hotrod.livesql.expressions.numeric.NumericExpression;

public class DateTimeLead extends DateTimeFunction implements PositionalAnalyticFunction {

  public DateTimeLead(final DateTimeExpression expression) {
    super("lead(#{})", expression);
  }

  public DateTimeLead(final DateTimeExpression expression, final NumericExpression offset) {
    super("lead(#{}, #{})", expression, offset);
  }

  public DateTimeLead(final DateTimeExpression expression, final NumericExpression offset,
      final DateTimeExpression defaultValue) {
    super("lead(#{}, #{}, #{})", expression, offset, defaultValue);
  }

  public DateTimeWindowFunctionOverStage over() {
    return new DateTimeWindowFunctionOverStage(new DateTimeWindowExpression(this));
  }

}
