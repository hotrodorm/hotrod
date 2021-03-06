package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;
import org.hotrod.runtime.livesql.expressions.object.ObjectExpression;
import org.hotrod.runtime.livesql.expressions.object.ObjectFunction;

public class ObjectLead extends ObjectFunction implements PositionalAnalyticFunction {

  public ObjectLead(final ObjectExpression expression) {
    super("lead(#{})", expression);
  }

  public ObjectLead(final ObjectExpression expression, final NumberExpression offset) {
    super("lead(#{}, #{})", expression, offset);
  }

  public ObjectLead(final ObjectExpression expression, final NumberExpression offset,
      final ObjectExpression defaultValue) {
    super("lead(#{}, #{}, #{})", expression, offset, defaultValue);
  }

  public ObjectWindowFunctionOverStage over() {
    return new ObjectWindowFunctionOverStage(new ObjectWindowExpression(this));
  }

}
