package org.hotrod.runtime.livesql.expressions.aggregations;

import org.hotrod.runtime.livesql.expressions.binary.ByteArrayExpression;

public class ByteArrayMin extends ByteArrayAggregationFunction {

  public ByteArrayMin(final ByteArrayExpression expression) {
    super("min", expression);
  }

}
