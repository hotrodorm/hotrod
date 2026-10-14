package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.numeric.NumericFunction;
import org.hotrod.livesql.queries.QueryWriter;

public class DenseRank extends NumericFunction implements AnalyticFunction {

  public DenseRank() {
    super();
  }

  public NumericWindowFunctionOverStage over() {
    return new NumericWindowFunctionOverStage(new NumericWindowExpression(this));
  }

  @Override
  protected void renderTo(QueryWriter w) {
    w.write("dense_rank()");
  }

}
