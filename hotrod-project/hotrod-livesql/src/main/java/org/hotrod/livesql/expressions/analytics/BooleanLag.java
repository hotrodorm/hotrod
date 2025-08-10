package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.bool.BooleanExpression;
import org.hotrod.livesql.expressions.character.CharFunction;
import org.hotrod.livesql.expressions.numeric.NumericExpression;

public class BooleanLag extends CharFunction implements PositionalAnalyticFunction {

  public BooleanLag(final BooleanExpression expression) {
    super("lag(#{})", expression);
  }

  public BooleanLag(final BooleanExpression expression, final NumericExpression offset) {
    super("lag(#{}, #{})", expression, offset);
  }

  public BooleanLag(final BooleanExpression expression, final NumericExpression offset, final BooleanExpression defaultValue) {
    super("lag(#{}, #{}, #{})", expression, offset, defaultValue);
  }

  public BooleanWindowFunctionOverStage over() {
    return new BooleanWindowFunctionOverStage(new BooleanWindowExpression(this));
  }

}
