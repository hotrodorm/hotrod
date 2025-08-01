package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.numeric.GeneralNumericExpression;
import org.hotrod.livesql.expressions.object.GeneralObjectExpression;
import org.hotrod.livesql.expressions.object.ObjectFunction;

public class ObjectLag extends ObjectFunction implements PositionalAnalyticFunction {

  public ObjectLag(final GeneralObjectExpression expression) {
    super("lag(#{})", expression);
  }

  public ObjectLag(final GeneralObjectExpression expression, final GeneralNumericExpression offset) {
    super("lag(#{}, #{})", expression, offset);
  }

  public ObjectLag(final GeneralObjectExpression expression, final GeneralNumericExpression offset,
      final GeneralObjectExpression defaultValue) {
    super("lag(#{}, #{}, #{})", expression, offset, defaultValue);
  }

  public ObjectWindowFunctionOverStage over() {
    return new ObjectWindowFunctionOverStage(new ObjectWindowExpression(this));
  }

}
