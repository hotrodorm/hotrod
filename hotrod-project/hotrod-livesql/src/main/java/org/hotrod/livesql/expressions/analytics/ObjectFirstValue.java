package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.object.ObjectExpression;
import org.hotrod.livesql.expressions.object.ObjectFunction;

public class ObjectFirstValue extends ObjectFunction implements PositionalAnalyticFunction {

  public ObjectFirstValue(final ObjectExpression expression) {
    super("first_value(#{})", expression);
  }

  public ObjectWindowFunctionOverStage over() {
    return new ObjectWindowFunctionOverStage(new ObjectWindowExpression(this));
  }

}
