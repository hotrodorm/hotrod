package org.hotrod.livesql.expressions.aggregations;

import org.hotrod.livesql.expressions.analytics.BooleanWindowExpression;
import org.hotrod.livesql.expressions.analytics.BooleanWindowFunctionOverStage;
import org.hotrod.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.livesql.expressions.bool.BooleanFunction;
import org.hotrod.livesql.expressions.bool.BooleanExpression;

public class BooleanMin extends BooleanFunction implements WindowableAggregationFunction {

  public BooleanMin(final BooleanExpression expression) {
    super("min(#{})", expression);
  }

  public BooleanWindowFunctionOverStage over() {
    return new BooleanWindowFunctionOverStage(new BooleanWindowExpression(this));
  }

}
