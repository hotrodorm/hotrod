package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.bool.GeneralBooleanExpression;
import org.hotrod.livesql.expressions.character.CharFunction;
import org.hotrod.livesql.expressions.numeric.GeneralNumericExpression;

public class BooleanLag extends CharFunction implements PositionalAnalyticFunction {

  public BooleanLag(final GeneralBooleanExpression expression) {
    super("lag(#{})", expression);
  }

  public BooleanLag(final GeneralBooleanExpression expression, final GeneralNumericExpression offset) {
    super("lag(#{}, #{})", expression, offset);
  }

  public BooleanLag(final GeneralBooleanExpression expression, final GeneralNumericExpression offset, final GeneralBooleanExpression defaultValue) {
    super("lag(#{}, #{}, #{})", expression, offset, defaultValue);
  }

  public BooleanWindowFunctionOverStage over() {
    return new BooleanWindowFunctionOverStage(new BooleanWindowExpression(this));
  }

}
