package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.numeric.NumericFunction;
import org.hotrod.livesql.queries.QueryWriter;

public class Rank extends NumericFunction implements AnalyticFunction {

  public Rank() {
    super();
  }

  public NumericWindowFunctionOverStage over() {
    return new NumericWindowFunctionOverStage(new NumericWindowExpression(this));
  }

  @Override
  protected void renderTo(QueryWriter w) {
    w.write("rank()");
  }

}
