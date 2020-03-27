package org.hotrod.runtime.livesql.expressions.aggregations;

import org.hotrod.runtime.livesql.expressions.binary.ByteArrayExpression;

public class ByteArrayMax extends ByteArrayAggregationFunction {

  public ByteArrayMax(final ByteArrayExpression expression) {
    super("max", expression);
  }

}
