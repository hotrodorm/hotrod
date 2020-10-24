package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;
import org.hotrod.runtime.livesql.expressions.strings.StringExpression;
import org.hotrod.runtime.livesql.expressions.strings.StringFunction;

public class StringLag extends StringFunction implements PositionalAnalyticFunction {

  public StringLag(final StringExpression expression, final NumberExpression offset,
      final StringExpression defaultValue) {
    super("lag", expression, offset, defaultValue);
  }

  public StringWindowFunctionOverStage over() {
    return new StringWindowFunctionOverStage(new StringWindowExpression(this));
  }

}
