package org.hotrod.livesql.expressions.aggregations;

import org.hotrod.livesql.expressions.analytics.BooleanWindowExpression;
import org.hotrod.livesql.expressions.analytics.BooleanWindowFunctionOverStage;
import org.hotrod.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.livesql.expressions.predicates.BooleanFunction;
import org.hotrod.livesql.expressions.predicates.GeneralBooleanExpression;

public class BooleanMax extends BooleanFunction implements WindowableAggregationFunction {

  public BooleanMax(final GeneralBooleanExpression expression) {
    super("max(#{})", expression);
  }

  public BooleanWindowFunctionOverStage over() {
    return new BooleanWindowFunctionOverStage(new BooleanWindowExpression(this));
  }

}
