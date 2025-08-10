package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.numeric.NumericExpression;
import org.hotrod.livesql.expressions.object.ObjectExpression;
import org.hotrod.livesql.expressions.object.ObjectFunction;

public class ObjectLag extends ObjectFunction implements PositionalAnalyticFunction {

  public ObjectLag(final ObjectExpression expression) {
    super("lag(#{})", expression);
  }

  public ObjectLag(final ObjectExpression expression, final NumericExpression offset) {
    super("lag(#{}, #{})", expression, offset);
  }

  public ObjectLag(final ObjectExpression expression, final NumericExpression offset,
      final ObjectExpression defaultValue) {
    super("lag(#{}, #{}, #{})", expression, offset, defaultValue);
  }

  public ObjectWindowFunctionOverStage over() {
    return new ObjectWindowFunctionOverStage(new ObjectWindowExpression(this));
  }

}
