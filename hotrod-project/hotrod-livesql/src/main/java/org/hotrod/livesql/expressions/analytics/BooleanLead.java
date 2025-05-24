package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.numbers.GeneralNumberExpression;
import org.hotrod.livesql.expressions.predicates.BooleanFunction;
import org.hotrod.livesql.expressions.predicates.GeneralBooleanExpression;

public class BooleanLead extends BooleanFunction implements PositionalAnalyticFunction {

  public BooleanLead(final GeneralBooleanExpression expression) {
    super("lead(#{})", expression);
  }

  public BooleanLead(final GeneralBooleanExpression expression, final GeneralNumberExpression offset) {
    super("lead(#{}, #{})", expression, offset);
  }

  public BooleanLead(final GeneralBooleanExpression expression, final GeneralNumberExpression offset, final GeneralBooleanExpression defaultValue) {
    super("lead(#{}, #{}, #{})", expression, offset, defaultValue);
  }

  public BooleanWindowFunctionOverStage over() {
    return new BooleanWindowFunctionOverStage(new BooleanWindowExpression(this));
  }

}
