package org.hotrod.runtime.livesql.expressions.aggregations;

import org.hotrod.runtime.livesql.expressions.object.ObjectExpression;

public class ObjectMin extends ObjectAggregationFunction {

  public ObjectMin(final ObjectExpression expression) {
    super("min", expression);
  }

}
