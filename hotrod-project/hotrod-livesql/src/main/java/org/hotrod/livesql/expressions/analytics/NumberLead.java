package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.numbers.GeneralNumberExpression;
import org.hotrod.livesql.expressions.numbers.NumberFunction;

public class NumberLead extends NumberFunction implements PositionalAnalyticFunction {

  public NumberLead(final GeneralNumberExpression expression) {
    super("lead(#{})", expression);
  }

  public NumberLead(final GeneralNumberExpression expression, final GeneralNumberExpression offset) {
    super("lead(#{}, #{})", expression, offset);
  }

  public NumberLead(final GeneralNumberExpression expression, final GeneralNumberExpression offset,
      final GeneralNumberExpression defaultValue) {
    super("lead(#{}, #{}, #{})", expression, offset, defaultValue);
  }

  public NumberWindowFunctionOverStage over() {
    return new NumberWindowFunctionOverStage(new NumberWindowExpression(this));
  }

}
