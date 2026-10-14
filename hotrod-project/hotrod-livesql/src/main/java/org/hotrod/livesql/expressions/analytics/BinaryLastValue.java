package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.binary.BinaryExpression;
import org.hotrod.livesql.expressions.binary.BinaryFunction;

public class BinaryLastValue extends BinaryFunction implements PositionalAnalyticFunction {

  public BinaryLastValue(final BinaryExpression expression) {
    super("last_value(#{})", expression);
  }

  public BinaryWindowFunctionOverStage over() {
    return new BinaryWindowFunctionOverStage(new BinaryWindowExpression(this));
  }

}
