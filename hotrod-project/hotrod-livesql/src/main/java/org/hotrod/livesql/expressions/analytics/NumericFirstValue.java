package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.numeric.NumericExpression;
import org.hotrod.livesql.expressions.numeric.NumericFunction;

public class NumericFirstValue extends NumericFunction implements PositionalAnalyticFunction {

  public NumericFirstValue(final NumericExpression expression) {
    super("first_value(#{})", expression);
  }

  public NumericWindowFunctionOverStage over() {
    return new NumericWindowFunctionOverStage(new NumericWindowExpression(this));
  }

}
