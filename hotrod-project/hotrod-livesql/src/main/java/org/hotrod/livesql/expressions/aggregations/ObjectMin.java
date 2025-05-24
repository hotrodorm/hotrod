package org.hotrod.livesql.expressions.aggregations;

import org.hotrod.livesql.expressions.analytics.ObjectWindowExpression;
import org.hotrod.livesql.expressions.analytics.ObjectWindowFunctionOverStage;
import org.hotrod.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.livesql.expressions.object.GeneralObjectExpression;
import org.hotrod.livesql.expressions.object.ObjectFunction;

public class ObjectMin extends ObjectFunction implements WindowableAggregationFunction {

  public ObjectMin(final GeneralObjectExpression expression) {
    super("min(#{})", expression);
  }

  public ObjectWindowFunctionOverStage over() {
    return new ObjectWindowFunctionOverStage(new ObjectWindowExpression(this));
  }

}
