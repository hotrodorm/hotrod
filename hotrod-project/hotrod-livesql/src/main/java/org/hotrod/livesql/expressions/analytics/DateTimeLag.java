package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.datetime.DateTimeFunction;
import org.hotrod.livesql.expressions.datetime.GeneralDateTimeExpression;
import org.hotrod.livesql.expressions.numeric.GeneralNumericExpression;

public class DateTimeLag extends DateTimeFunction implements PositionalAnalyticFunction {

  public DateTimeLag(final GeneralDateTimeExpression expression) {
    super("lag(#{})", expression);
  }

  public DateTimeLag(final GeneralDateTimeExpression expression, final GeneralNumericExpression offset) {
    super("lag(#{}, #{})", expression, offset);
  }

  public DateTimeLag(final GeneralDateTimeExpression expression, final GeneralNumericExpression offset,
      final GeneralDateTimeExpression defaultValue) {
    super("lag(#{}, #{}, #{})", expression, offset, defaultValue);
  }

  public DateTimeWindowFunctionOverStage over() {
    return new DateTimeWindowFunctionOverStage(new DateTimeWindowExpression(this));
  }

}
