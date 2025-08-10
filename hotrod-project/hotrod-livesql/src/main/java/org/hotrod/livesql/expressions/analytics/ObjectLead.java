package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.numeric.NumericExpression;
import org.hotrod.livesql.expressions.object.ObjectExpression;
import org.hotrod.livesql.expressions.object.ObjectFunction;

public class ObjectLead extends ObjectFunction implements PositionalAnalyticFunction {

  public ObjectLead(final ObjectExpression expression) {
    super("lead(#{})", expression);
  }

  public ObjectLead(final ObjectExpression expression, final NumericExpression offset) {
    super("lead(#{}, #{})", expression, offset);
  }

  public ObjectLead(final ObjectExpression expression, final NumericExpression offset,
      final ObjectExpression defaultValue) {
    super("lead(#{}, #{}, #{})", expression, offset, defaultValue);
  }

  public ObjectWindowFunctionOverStage over() {
    return new ObjectWindowFunctionOverStage(new ObjectWindowExpression(this));
  }

}
