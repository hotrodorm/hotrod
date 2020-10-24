package org.hotrod.runtime.livesql.expressions.aggregations;

import org.hotrod.runtime.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.runtime.livesql.expressions.binary.ByteArrayExpression;
import org.hotrod.runtime.livesql.expressions.binary.ByteArrayFunction;

public class ByteArrayMin extends ByteArrayFunction implements WindowableAggregationFunction {

  public ByteArrayMin(final ByteArrayExpression expression) {
    super("min", expression);
  }

}
