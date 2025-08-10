package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.datetime.DateTimeFunction;
import org.hotrod.livesql.expressions.datetime.DateTimeExpression;
import org.hotrod.livesql.expressions.numeric.NumericExpression;

public class DateTimeLag extends DateTimeFunction implements PositionalAnalyticFunction {

  public DateTimeLag(final DateTimeExpression expression) {
    super("lag(#{})", expression);
  }

  public DateTimeLag(final DateTimeExpression expression, final NumericExpression offset) {
    super("lag(#{}, #{})", expression, offset);
  }

  public DateTimeLag(final DateTimeExpression expression, final NumericExpression offset,
      final DateTimeExpression defaultValue) {
    super("lag(#{}, #{}, #{})", expression, offset, defaultValue);
  }

  public DateTimeWindowFunctionOverStage over() {
    return new DateTimeWindowFunctionOverStage(new DateTimeWindowExpression(this));
  }

}
