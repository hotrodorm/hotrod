package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.numeric.NumericExpression;
import org.hotrod.livesql.expressions.numeric.NumericFunction;

public class NumericLead extends NumericFunction implements PositionalAnalyticFunction {

  public NumericLead(final NumericExpression expression) {
    super("lead(#{})", expression);
  }

  public NumericLead(final NumericExpression expression, final NumericExpression offset) {
    super("lead(#{}, #{})", expression, offset);
  }

  public NumericLead(final NumericExpression expression, final NumericExpression offset,
      final NumericExpression defaultValue) {
    super("lead(#{}, #{}, #{})", expression, offset, defaultValue);
  }

  public NumericWindowFunctionOverStage over() {
    return new NumericWindowFunctionOverStage(new NumericWindowExpression(this));
  }

}
