package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.numeric.GeneralNumericExpression;
import org.hotrod.livesql.expressions.numeric.NumericFunction;

public class NumericLag extends NumericFunction implements PositionalAnalyticFunction {

  public NumericLag(final GeneralNumericExpression expression) {
    super("lag(#{})", expression);
  }

  public NumericLag(final GeneralNumericExpression expression, final GeneralNumericExpression offset) {
    super("lag(#{}, #{})", expression, offset);
  }

  public NumericLag(final GeneralNumericExpression expression, final GeneralNumericExpression offset,
      final GeneralNumericExpression defaultValue) {
    super("lag(#{}, #{}, #{})", expression, offset, defaultValue);
  }

  public NumericWindowFunctionOverStage over() {
    return new NumericWindowFunctionOverStage(new NumericWindowExpression(this));
  }

}
