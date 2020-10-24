package org.hotrod.runtime.livesql.expressions.aggregations;

import org.hotrod.runtime.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.runtime.livesql.expressions.object.ObjectExpression;
import org.hotrod.runtime.livesql.expressions.object.ObjectFunction;

public class ObjectMax extends ObjectFunction implements WindowableAggregationFunction {

  public ObjectMax(final ObjectExpression expression) {
    super("max", expression);
  }

}
