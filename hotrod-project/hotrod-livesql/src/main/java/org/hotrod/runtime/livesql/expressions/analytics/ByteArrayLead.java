package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.binary.ByteArrayExpression;
import org.hotrod.runtime.livesql.expressions.binary.ByteArrayFunction;
import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;

public class ByteArrayLead extends ByteArrayFunction implements PositionalAnalyticFunction {

  public ByteArrayLead(final ByteArrayExpression expression, final NumberExpression offset,
      final ByteArrayExpression defaultValue) {
    super("lead", expression, offset, defaultValue);
  }

  public ByteArrayWindowFunctionOverStage over() {
    return new ByteArrayWindowFunctionOverStage(new ByteArrayWindowExpression(this));
  }

}
