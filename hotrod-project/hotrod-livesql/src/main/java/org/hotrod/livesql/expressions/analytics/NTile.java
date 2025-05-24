package org.hotrod.livesql.expressions.analytics;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.numbers.NumberFunction;

public class NTile extends NumberFunction implements AnalyticFunction {

  public NTile(final ComparableExpression expression) {
    super("ntile(#{})", expression);
  }

  public NumberWindowFunctionOverStage over() {
    return new NumberWindowFunctionOverStage(new NumberWindowExpression(this));
  }

}
