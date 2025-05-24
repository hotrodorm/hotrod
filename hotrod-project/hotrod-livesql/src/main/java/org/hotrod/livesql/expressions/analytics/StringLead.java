package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.numbers.GeneralNumberExpression;
import org.hotrod.livesql.expressions.strings.GeneralStringExpression;
import org.hotrod.livesql.expressions.strings.StringFunction;

public class StringLead extends StringFunction implements PositionalAnalyticFunction {

  public StringLead(final GeneralStringExpression expression) {
    super("lead(#{})", expression);
  }

  public StringLead(final GeneralStringExpression expression, final GeneralNumberExpression offset) {
    super("lead(#{}, #{})", expression, offset);
  }

  public StringLead(final GeneralStringExpression expression, final GeneralNumberExpression offset,
      final GeneralStringExpression defaultValue) {
    super("lead(#{}, #{}, #{})", expression, offset, defaultValue);
  }

  public StringWindowFunctionOverStage over() {
    return new StringWindowFunctionOverStage(new StringWindowExpression(this));
  }

}
