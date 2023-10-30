package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.ComparableExpression;
import org.hotrod.runtime.livesql.expressions.numbers.NumberFunction;

public class DenseRank extends NumberFunction implements AnalyticFunction {

  public DenseRank(final ComparableExpression expression) {
    super("dense_rank(#{})", expression);
  }

  public NumberWindowFunctionOverStage over() {
    return new NumberWindowFunctionOverStage(new NumberWindowExpression(this));
  }

}
