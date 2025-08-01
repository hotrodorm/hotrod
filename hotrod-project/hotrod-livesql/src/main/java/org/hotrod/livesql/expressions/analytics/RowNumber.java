package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.numeric.NumericFunction;

public class RowNumber extends NumericFunction implements AnalyticFunction {

  public RowNumber() {
    super("row_number()");
  }

  public NumericWindowFunctionOverStage over() {
    return new NumericWindowFunctionOverStage(new NumericWindowExpression(this));
  }

}
