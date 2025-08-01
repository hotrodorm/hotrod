package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.numeric.NumericFunction;

public class DenseRank extends NumericFunction implements AnalyticFunction {

  public DenseRank(final ComparableExpression expression) {
    super("dense_rank(#{})", expression);
  }

  public NumericWindowFunctionOverStage over() {
    return new NumericWindowFunctionOverStage(new NumericWindowExpression(this));
  }

}
