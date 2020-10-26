package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;
import org.hotrod.runtime.livesql.expressions.numbers.NumberFunction;

public class NumberLead extends NumberFunction implements PositionalAnalyticFunction {

  public NumberLead(final NumberExpression expression) {
    super("lead(#{})", expression);
  }

  public NumberLead(final NumberExpression expression, final NumberExpression offset) {
    super("lead(#{}, #{})", expression, offset);
  }

  public NumberLead(final NumberExpression expression, final NumberExpression offset,
      final NumberExpression defaultValue) {
    super("lead(#{}, #{}, #{})", expression, offset, defaultValue);
  }

  public NumberWindowFunctionOverStage over() {
    return new NumberWindowFunctionOverStage(new NumberWindowExpression(this));
  }

}
