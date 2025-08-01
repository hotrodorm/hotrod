package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.bool.BooleanFunction;
import org.hotrod.livesql.expressions.bool.GeneralBooleanExpression;
import org.hotrod.livesql.expressions.numeric.GeneralNumericExpression;

public class BooleanLead extends BooleanFunction implements PositionalAnalyticFunction {

  public BooleanLead(final GeneralBooleanExpression expression) {
    super("lead(#{})", expression);
  }

  public BooleanLead(final GeneralBooleanExpression expression, final GeneralNumericExpression offset) {
    super("lead(#{}, #{})", expression, offset);
  }

  public BooleanLead(final GeneralBooleanExpression expression, final GeneralNumericExpression offset, final GeneralBooleanExpression defaultValue) {
    super("lead(#{}, #{}, #{})", expression, offset, defaultValue);
  }

  public BooleanWindowFunctionOverStage over() {
    return new BooleanWindowFunctionOverStage(new BooleanWindowExpression(this));
  }

}
