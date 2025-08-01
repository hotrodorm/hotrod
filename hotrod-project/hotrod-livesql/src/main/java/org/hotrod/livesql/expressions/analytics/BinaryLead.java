package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.binary.BinaryFunction;
import org.hotrod.livesql.expressions.binary.GeneralBinaryExpression;
import org.hotrod.livesql.expressions.numeric.GeneralNumericExpression;

public class BinaryLead extends BinaryFunction implements PositionalAnalyticFunction {

  public BinaryLead(final GeneralBinaryExpression expression) {
    super("lead(#{})", expression);
  }

  public BinaryLead(final GeneralBinaryExpression expression, final GeneralNumericExpression offset) {
    super("lead(#{}, #{})", expression, offset);
  }

  public BinaryLead(final GeneralBinaryExpression expression, final GeneralNumericExpression offset,
      final GeneralBinaryExpression defaultValue) {
    super("lead(#{}, #{}, #{})", expression, offset, defaultValue);
  }

  public BinaryWindowFunctionOverStage over() {
    return new BinaryWindowFunctionOverStage(new BinaryWindowExpression(this));
  }

}
