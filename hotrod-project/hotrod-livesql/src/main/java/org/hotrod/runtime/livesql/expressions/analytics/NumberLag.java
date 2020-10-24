package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;
import org.hotrod.runtime.livesql.expressions.numbers.NumberFunction;

public class NumberLag extends NumberFunction implements PositionalAnalyticFunction {

  public NumberLag(final NumberExpression expression, final NumberExpression offset,
      final NumberExpression defaultValue) {
    super("lag", expression, offset, defaultValue);
  }

  public NumberWindowFunctionOverStage over() {
    return new NumberWindowFunctionOverStage(new WindowExpression(this));
  }

}
