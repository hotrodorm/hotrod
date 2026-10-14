package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.numeric.NumericFunction;
import org.hotrod.livesql.queries.QueryWriter;

public class RowNumber extends NumericFunction implements AnalyticFunction {

  public RowNumber() {
    super();
  }

  public NumericWindowFunctionOverStage over() {
    return new NumericWindowFunctionOverStage(new NumericWindowExpression(this));
  }

  @Override
  protected void renderTo(QueryWriter w) {
    w.write("row_number()");
  }

}
