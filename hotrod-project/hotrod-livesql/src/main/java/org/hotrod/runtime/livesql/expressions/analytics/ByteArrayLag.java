package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.binary.GeneralByteArrayExpression;
import org.hotrod.runtime.livesql.expressions.numbers.GeneralNumberExpression;
import org.hotrod.runtime.livesql.expressions.strings.StringFunction;

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
