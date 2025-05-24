package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.numbers.NumberFunction;

public class Rank extends NumberFunction implements AnalyticFunction {

  public Rank(final ComparableExpression expression) {
    super("rank(#{})", expression);
  }

  public NumberWindowFunctionOverStage over() {
    return new NumberWindowFunctionOverStage(new NumberWindowExpression(this));
  }

}
