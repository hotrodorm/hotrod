package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.binary.BinaryFunction;
import org.hotrod.livesql.expressions.binary.BinaryExpression;
import org.hotrod.livesql.expressions.numeric.NumericExpression;

public class BinaryLead extends BinaryFunction implements PositionalAnalyticFunction {

  public BinaryLead(final BinaryExpression expression) {
    super("lead(#{})", expression);
  }

  public BinaryLead(final BinaryExpression expression, final NumericExpression offset) {
    super("lead(#{}, #{})", expression, offset);
  }

  public BinaryLead(final BinaryExpression expression, final NumericExpression offset,
      final BinaryExpression defaultValue) {
    super("lead(#{}, #{}, #{})", expression, offset, defaultValue);
  }

  public BinaryWindowFunctionOverStage over() {
    return new BinaryWindowFunctionOverStage(new BinaryWindowExpression(this));
  }

}
