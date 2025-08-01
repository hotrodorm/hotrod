package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.numeric.GeneralNumericExpression;
import org.hotrod.livesql.expressions.numeric.NumericFunction;

public class NumericLead extends NumericFunction implements PositionalAnalyticFunction {

  public NumericLead(final GeneralNumericExpression expression) {
    super("lead(#{})", expression);
  }

  public NumericLead(final GeneralNumericExpression expression, final GeneralNumericExpression offset) {
    super("lead(#{}, #{})", expression, offset);
  }

  public NumericLead(final GeneralNumericExpression expression, final GeneralNumericExpression offset,
      final GeneralNumericExpression defaultValue) {
    super("lead(#{}, #{}, #{})", expression, offset, defaultValue);
  }

  public NumericWindowFunctionOverStage over() {
    return new NumericWindowFunctionOverStage(new NumericWindowExpression(this));
  }

}
