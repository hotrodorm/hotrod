package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;
import org.hotrod.runtime.livesql.expressions.strings.StringExpression;
import org.hotrod.runtime.livesql.expressions.strings.StringFunction;

public class StringLead extends StringFunction implements PositionalAnalyticFunction {

  public StringLead(final StringExpression expression) {
    super("lead(#{})", expression);
  }

  public StringLead(final StringExpression expression, final NumberExpression offset) {
    super("lead(#{}, #{})", expression, offset);
  }

  public StringLead(final StringExpression expression, final NumberExpression offset,
      final StringExpression defaultValue) {
    super("lead(#{}, #{}, #{})", expression, offset, defaultValue);
  }

  public StringWindowFunctionOverStage over() {
    return new StringWindowFunctionOverStage(new StringWindowExpression(this));
  }

}
