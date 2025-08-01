package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.binary.GeneralBinaryExpression;
import org.hotrod.livesql.expressions.character.CharFunction;
import org.hotrod.livesql.expressions.numeric.GeneralNumericExpression;

public class BinaryLag extends CharFunction implements PositionalAnalyticFunction {

  public BinaryLag(final GeneralBinaryExpression expression) {
    super("lag(#{})", expression);
  }

  public BinaryLag(final GeneralBinaryExpression expression, final GeneralNumericExpression offset) {
    super("lag(#{}, #{})", expression, offset);
  }

  public BinaryLag(final GeneralBinaryExpression expression, final GeneralNumericExpression offset,
      final GeneralBinaryExpression defaultValue) {
    super("lag(#{}, #{}, #{})", expression, offset, defaultValue);
  }

  public BinaryWindowFunctionOverStage over() {
    return new BinaryWindowFunctionOverStage(new BinaryWindowExpression(this));
  }

}
