package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.numbers.GeneralNumberExpression;
import org.hotrod.livesql.expressions.strings.GeneralStringExpression;
import org.hotrod.livesql.expressions.strings.StringFunction;

public class StringLag extends StringFunction implements PositionalAnalyticFunction {

  public StringLag(final GeneralStringExpression expression) {
    super("lag(#{})", expression);
  }

  public StringLag(final GeneralStringExpression expression, final GeneralNumberExpression offset) {
    super("lag(#{}, #{})", expression, offset);
  }

  public StringLag(final GeneralStringExpression expression, final GeneralNumberExpression offset,
      final GeneralStringExpression defaultValue) {
    super("lag(#{}, #{}, #{})", expression, offset, defaultValue);
  }

  public StringWindowFunctionOverStage over() {
    return new StringWindowFunctionOverStage(new StringWindowExpression(this));
  }

}
