package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.numbers.GeneralNumberExpression;
import org.hotrod.runtime.livesql.expressions.object.GeneralObjectExpression;
import org.hotrod.runtime.livesql.expressions.object.ObjectFunction;

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
