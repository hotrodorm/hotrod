package org.hotrod.runtime.livesql.expressions.aggregations;

import org.hotrod.runtime.livesql.expressions.analytics.ObjectWindowExpression;
import org.hotrod.runtime.livesql.expressions.analytics.ObjectWindowFunctionOverStage;
import org.hotrod.runtime.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.runtime.livesql.expressions.object.ObjectExpression;
import org.hotrod.runtime.livesql.expressions.object.ObjectFunction;

public class ObjectMin extends ObjectFunction implements WindowableAggregationFunction {

  public ObjectMin(final ObjectExpression expression) {
    super("min", expression);
  }

  public ObjectWindowFunctionOverStage over() {
    return new ObjectWindowFunctionOverStage(new ObjectWindowExpression(this));
  }

}
