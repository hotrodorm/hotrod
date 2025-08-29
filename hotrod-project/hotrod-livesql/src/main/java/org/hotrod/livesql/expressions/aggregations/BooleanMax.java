package org.hotrod.livesql.expressions.aggregations;

import org.hotrod.livesql.expressions.analytics.BooleanWindowExpression;
import org.hotrod.livesql.expressions.analytics.BooleanWindowFunctionOverStage;
import org.hotrod.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.livesql.expressions.bool.BooleanFunction;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class BooleanMax extends BooleanFunction implements WindowableAggregationFunction {

  public BooleanMax(final Predicate expression) {
    super("max(#{})", expression);
  }

  public BooleanWindowFunctionOverStage over() {
    return new BooleanWindowFunctionOverStage(new BooleanWindowExpression(this));
  }

}
