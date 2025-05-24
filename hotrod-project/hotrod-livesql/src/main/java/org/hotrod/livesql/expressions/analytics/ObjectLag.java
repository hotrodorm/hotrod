package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.numbers.GeneralNumberExpression;
import org.hotrod.livesql.expressions.object.GeneralObjectExpression;
import org.hotrod.livesql.expressions.object.ObjectFunction;

public class ObjectLag extends ObjectFunction implements PositionalAnalyticFunction {

  public ObjectLag(final GeneralObjectExpression expression) {
    super("lag(#{})", expression);
  }

  public ObjectLag(final GeneralObjectExpression expression, final GeneralNumberExpression offset) {
    super("lag(#{}, #{})", expression, offset);
  }

  public ObjectLag(final GeneralObjectExpression expression, final GeneralNumberExpression offset,
      final GeneralObjectExpression defaultValue) {
    super("lag(#{}, #{}, #{})", expression, offset, defaultValue);
  }

  public ObjectWindowFunctionOverStage over() {
    return new ObjectWindowFunctionOverStage(new ObjectWindowExpression(this));
  }

}
