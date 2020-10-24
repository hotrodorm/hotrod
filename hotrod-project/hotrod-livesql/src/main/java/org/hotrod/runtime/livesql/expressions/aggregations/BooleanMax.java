package org.hotrod.runtime.livesql.expressions.aggregations;

import org.hotrod.runtime.livesql.expressions.analytics.BooleanWindowExpression;
import org.hotrod.runtime.livesql.expressions.analytics.BooleanWindowFunctionOverStage;
import org.hotrod.runtime.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.runtime.livesql.expressions.predicates.BooleanFunction;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class BooleanMax extends BooleanFunction implements WindowableAggregationFunction {

  public BooleanMax(final Predicate expression) {
    super("max", expression);
  }

  public BooleanWindowFunctionOverStage over() {
    return new BooleanWindowFunctionOverStage(new BooleanWindowExpression(this));
  }

}
