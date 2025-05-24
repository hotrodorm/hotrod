package org.hotrod.livesql.expressions.aggregations;

import org.hotrod.livesql.expressions.analytics.ObjectWindowExpression;
import org.hotrod.livesql.expressions.analytics.ObjectWindowFunctionOverStage;
import org.hotrod.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.livesql.expressions.object.GeneralObjectExpression;
import org.hotrod.livesql.expressions.object.ObjectFunction;

public class ObjectMax extends ObjectFunction implements WindowableAggregationFunction {

  public ObjectMax(final GeneralObjectExpression expression) {
    super("max(#{})", expression);
  }

  public ObjectWindowFunctionOverStage over() {
    return new ObjectWindowFunctionOverStage(new ObjectWindowExpression(this));
  }

}
