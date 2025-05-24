package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.numbers.GeneralNumberExpression;
import org.hotrod.livesql.expressions.object.GeneralObjectExpression;
import org.hotrod.livesql.expressions.object.ObjectFunction;

public class ObjectLead extends ObjectFunction implements PositionalAnalyticFunction {

  public ObjectLead(final GeneralObjectExpression expression) {
    super("lead(#{})", expression);
  }

  public ObjectLead(final GeneralObjectExpression expression, final GeneralNumberExpression offset) {
    super("lead(#{}, #{})", expression, offset);
  }

  public ObjectLead(final GeneralObjectExpression expression, final GeneralNumberExpression offset,
      final GeneralObjectExpression defaultValue) {
    super("lead(#{}, #{}, #{})", expression, offset, defaultValue);
  }

  public ObjectWindowFunctionOverStage over() {
    return new ObjectWindowFunctionOverStage(new ObjectWindowExpression(this));
  }

}
