package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.binary.BinaryExpression;
import org.hotrod.livesql.expressions.character.CharFunction;

public class BinaryLastValue extends CharFunction implements PositionalAnalyticFunction {

  public BinaryLastValue(final BinaryExpression expression) {
    super("last_value(#{})", expression);
  }

  public BinaryWindowFunctionOverStage over() {
    return new BinaryWindowFunctionOverStage(new BinaryWindowExpression(this));
  }

}
