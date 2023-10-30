package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.ComparableExpression;
import org.hotrod.runtime.livesql.expressions.numbers.NumberFunction;

public class NTile extends NumberFunction implements AnalyticFunction {

  public NTile(final ComparableExpression expression) {
    super("ntile(#{})", expression);
  }

  public NumberWindowFunctionOverStage over() {
    return new NumberWindowFunctionOverStage(new NumberWindowExpression(this));
  }

}
