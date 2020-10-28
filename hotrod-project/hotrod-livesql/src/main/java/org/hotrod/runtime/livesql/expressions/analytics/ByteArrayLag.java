package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.binary.ByteArrayExpression;
import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;
import org.hotrod.runtime.livesql.expressions.strings.StringFunction;

public class ByteArrayLag extends StringFunction implements PositionalAnalyticFunction {

  public ByteArrayLag(final ByteArrayExpression expression) {
    super("lag(#{})", expression);
  }

  public ByteArrayLag(final ByteArrayExpression expression, final NumberExpression offset) {
    super("lag(#{}, #{})", expression, offset);
  }

  public ByteArrayLag(final ByteArrayExpression expression, final NumberExpression offset,
      final ByteArrayExpression defaultValue) {
    super("lag(#{}, #{}, #{})", expression, offset, defaultValue);
  }

  public ByteArrayWindowFunctionOverStage over() {
    return new ByteArrayWindowFunctionOverStage(new ByteArrayWindowExpression(this));
  }

}
