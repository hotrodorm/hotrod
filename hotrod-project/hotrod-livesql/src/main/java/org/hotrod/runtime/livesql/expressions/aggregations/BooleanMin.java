package org.hotrod.runtime.livesql.expressions.aggregations;

import org.hotrod.runtime.livesql.expressions.analytics.BooleanWindowFunctionOverStage;
import org.hotrod.runtime.livesql.expressions.analytics.WindowExpression;
import org.hotrod.runtime.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.runtime.livesql.expressions.predicates.BooleanFunction;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class BooleanMin extends BooleanFunction implements WindowableAggregationFunction {

  public BooleanMin(final Predicate expression) {
    super("min", expression);
  }

  public BooleanWindowFunctionOverStage over() {
    return new BooleanWindowFunctionOverStage(new WindowExpression(this));
  }

}
