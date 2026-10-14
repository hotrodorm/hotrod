package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.binary.BinaryExpression;
import org.hotrod.livesql.expressions.binary.BinaryFunction;
import org.hotrod.livesql.expressions.numeric.NumericExpression;

public class BinaryLag extends BinaryFunction implements PositionalAnalyticFunction {

  public BinaryLag(final BinaryExpression expression) {
    super("lag(#{})", expression);
  }

  public BinaryLag(final BinaryExpression expression, final NumericExpression offset) {
    super("lag(#{}, #{})", expression, offset);
  }

  public BinaryLag(final BinaryExpression expression, final NumericExpression offset,
      final BinaryExpression defaultValue) {
    super("lag(#{}, #{}, #{})", expression, offset, defaultValue);
  }

  public BinaryWindowFunctionOverStage over() {
    return new BinaryWindowFunctionOverStage(new BinaryWindowExpression(this));
  }

}
