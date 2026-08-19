package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.object.ObjectExpression;
import org.hotrod.livesql.expressions.object.ObjectFunction;

public class ObjectLastValue extends ObjectFunction implements PositionalAnalyticFunction {

  public ObjectLastValue(final ObjectExpression expression) {
    super("last_value(#{})", expression);
  }

  public ObjectWindowFunctionOverStage over() {
    return new ObjectWindowFunctionOverStage(new ObjectWindowExpression(this));
  }

}
