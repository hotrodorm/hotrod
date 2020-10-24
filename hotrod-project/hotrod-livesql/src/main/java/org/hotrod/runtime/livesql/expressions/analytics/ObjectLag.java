package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;
import org.hotrod.runtime.livesql.expressions.object.ObjectExpression;
import org.hotrod.runtime.livesql.expressions.object.ObjectFunction;

public class ObjectLag extends ObjectFunction implements PositionalAnalyticFunction {

  public ObjectLag(final ObjectExpression expression, final NumberExpression offset,
      final ObjectExpression defaultValue) {
    super("lag", expression, offset, defaultValue);
  }

  public ObjectWindowFunctionOverStage over() {
    return new ObjectWindowFunctionOverStage(new WindowExpression(this));
  }

}
