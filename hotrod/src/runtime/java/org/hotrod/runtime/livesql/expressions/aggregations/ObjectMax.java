package org.hotrod.runtime.livesql.expressions.aggregations;

import org.hotrod.runtime.livesql.expressions.object.ObjectExpression;

public class ObjectMax extends ObjectAggregationFunction {

  public ObjectMax(final ObjectExpression expression) {
    super("max", expression);
  }

}
