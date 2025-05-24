package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.binary.GeneralByteArrayExpression;
import org.hotrod.livesql.expressions.numbers.GeneralNumberExpression;
import org.hotrod.livesql.expressions.strings.StringFunction;

public class ByteArrayLag extends StringFunction implements PositionalAnalyticFunction {

  public ByteArrayLag(final GeneralByteArrayExpression expression) {
    super("lag(#{})", expression);
  }

  public ByteArrayLag(final GeneralByteArrayExpression expression, final GeneralNumberExpression offset) {
    super("lag(#{}, #{})", expression, offset);
  }

  public ByteArrayLag(final GeneralByteArrayExpression expression, final GeneralNumberExpression offset,
      final GeneralByteArrayExpression defaultValue) {
    super("lag(#{}, #{}, #{})", expression, offset, defaultValue);
  }

  public ByteArrayWindowFunctionOverStage over() {
    return new ByteArrayWindowFunctionOverStage(new ByteArrayWindowExpression(this));
  }

}
