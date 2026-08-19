package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.binary.BinaryExpression;
import org.hotrod.livesql.expressions.character.CharFunction;

public class BinaryFirstValue extends CharFunction implements PositionalAnalyticFunction {

  public BinaryFirstValue(final BinaryExpression expression) {
    super("first_value(#{})", expression);
  }

  public BinaryWindowFunctionOverStage over() {
    return new BinaryWindowFunctionOverStage(new BinaryWindowExpression(this));
  }

}
