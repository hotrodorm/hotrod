package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.bool.BooleanFunction;
import org.hotrod.livesql.expressions.bool.BooleanExpression;
import org.hotrod.livesql.expressions.numeric.NumericExpression;

public class BooleanLead extends BooleanFunction implements PositionalAnalyticFunction {

  public BooleanLead(final BooleanExpression expression) {
    super("lead(#{})", expression);
  }

  public BooleanLead(final BooleanExpression expression, final NumericExpression offset) {
    super("lead(#{}, #{})", expression, offset);
  }

  public BooleanLead(final BooleanExpression expression, final NumericExpression offset, final BooleanExpression defaultValue) {
    super("lead(#{}, #{}, #{})", expression, offset, defaultValue);
  }

  public BooleanWindowFunctionOverStage over() {
    return new BooleanWindowFunctionOverStage(new BooleanWindowExpression(this));
  }

}
