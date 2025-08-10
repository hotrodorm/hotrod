package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.numeric.NumericExpression;
import org.hotrod.livesql.expressions.numeric.NumericFunction;

public class NumericLag extends NumericFunction implements PositionalAnalyticFunction {

  public NumericLag(final NumericExpression expression) {
    super("lag(#{})", expression);
  }

  public NumericLag(final NumericExpression expression, final NumericExpression offset) {
    super("lag(#{}, #{})", expression, offset);
  }

  public NumericLag(final NumericExpression expression, final NumericExpression offset,
      final NumericExpression defaultValue) {
    super("lag(#{}, #{}, #{})", expression, offset, defaultValue);
  }

  public NumericWindowFunctionOverStage over() {
    return new NumericWindowFunctionOverStage(new NumericWindowExpression(this));
  }

}
