package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.numbers.NumberFunction;

public class RowNumber extends NumberFunction implements AnalyticFunction {

  public RowNumber() {
    super("row_number");
  }

  public NumberWindowFunctionOverStage over() {
    return new NumberWindowFunctionOverStage(new WindowExpression(this));
  }

}
