package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.character.CharExpression;
import org.hotrod.livesql.expressions.character.CharFunction;

public class CharFirstValue extends CharFunction implements PositionalAnalyticFunction {

  public CharFirstValue(final CharExpression expression) {
    super("first_value(#{})", expression);
  }

  public CharWindowFunctionOverStage over() {
    return new CharWindowFunctionOverStage(new CharWindowExpression(this));
  }

}
