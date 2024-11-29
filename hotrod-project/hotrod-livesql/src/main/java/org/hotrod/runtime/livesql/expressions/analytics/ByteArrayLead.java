package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.binary.ByteArrayFunction;
import org.hotrod.runtime.livesql.expressions.binary.GeneralByteArrayExpression;
import org.hotrod.runtime.livesql.expressions.numbers.GeneralNumberExpression;

public class ByteArrayLead extends ByteArrayFunction implements PositionalAnalyticFunction {

  public ByteArrayLead(final GeneralByteArrayExpression expression) {
    super("lead(#{})", expression);
  }

  public ByteArrayLead(final GeneralByteArrayExpression expression, final GeneralNumberExpression offset) {
    super("lead(#{}, #{})", expression, offset);
  }

  public ByteArrayLead(final GeneralByteArrayExpression expression, final GeneralNumberExpression offset,
      final GeneralByteArrayExpression defaultValue) {
    super("lead(#{}, #{}, #{})", expression, offset, defaultValue);
  }

  public ByteArrayWindowFunctionOverStage over() {
    return new ByteArrayWindowFunctionOverStage(new ByteArrayWindowExpression(this));
  }

}
