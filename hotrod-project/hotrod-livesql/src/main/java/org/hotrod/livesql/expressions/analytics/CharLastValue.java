package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.character.CharExpression;
import org.hotrod.livesql.expressions.character.CharFunction;

public class CharLastValue extends CharFunction implements PositionalAnalyticFunction {

  public CharLastValue(final CharExpression expression) {
    super("last_value(#{})", expression);
  }

  public CharWindowFunctionOverStage over() {
    return new CharWindowFunctionOverStage(new CharWindowExpression(this));
  }

}
