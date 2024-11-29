package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.numbers.GeneralNumberExpression;
import org.hotrod.runtime.livesql.expressions.numbers.NumberFunction;

public class NumberLag extends NumberFunction implements PositionalAnalyticFunction {

  public NumberLag(final GeneralNumberExpression expression) {
    super("lag(#{})", expression);
  }

  public NumberLag(final GeneralNumberExpression expression, final GeneralNumberExpression offset) {
    super("lag(#{}, #{})", expression, offset);
  }

  public NumberLag(final GeneralNumberExpression expression, final GeneralNumberExpression offset,
      final GeneralNumberExpression defaultValue) {
    super("lag(#{}, #{}, #{})", expression, offset, defaultValue);
  }

  public NumberWindowFunctionOverStage over() {
    return new NumberWindowFunctionOverStage(new NumberWindowExpression(this));
  }

}
