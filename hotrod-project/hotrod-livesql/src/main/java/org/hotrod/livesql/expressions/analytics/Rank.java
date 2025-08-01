package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.numeric.NumericFunction;

public class Rank extends NumericFunction implements AnalyticFunction {

  public Rank(final ComparableExpression expression) {
    super("rank(#{})", expression);
  }

  public NumericWindowFunctionOverStage over() {
    return new NumericWindowFunctionOverStage(new NumericWindowExpression(this));
  }

}
