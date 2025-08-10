package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.character.CharExpression;
import org.hotrod.livesql.expressions.character.CharFunction;
import org.hotrod.livesql.expressions.numeric.NumericExpression;

public class CharLag extends CharFunction implements PositionalAnalyticFunction {

  public CharLag(final CharExpression expression) {
    super("lag(#{})", expression);
  }

  public CharLag(final CharExpression expression, final NumericExpression offset) {
    super("lag(#{}, #{})", expression, offset);
  }

  public CharLag(final CharExpression expression, final NumericExpression offset,
      final CharExpression defaultValue) {
    super("lag(#{}, #{}, #{})", expression, offset, defaultValue);
  }

  public CharWindowFunctionOverStage over() {
    return new CharWindowFunctionOverStage(new CharWindowExpression(this));
  }

}
