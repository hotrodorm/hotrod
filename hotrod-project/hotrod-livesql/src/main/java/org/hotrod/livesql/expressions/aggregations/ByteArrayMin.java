package org.hotrod.livesql.expressions.aggregations;

import org.hotrod.livesql.expressions.analytics.ByteArrayWindowExpression;
import org.hotrod.livesql.expressions.analytics.ByteArrayWindowFunctionOverStage;
import org.hotrod.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.livesql.expressions.binary.ByteArrayFunction;
import org.hotrod.livesql.expressions.binary.GeneralByteArrayExpression;

public class ByteArrayMin extends ByteArrayFunction implements WindowableAggregationFunction {

  public ByteArrayMin(final GeneralByteArrayExpression expression) {
    super("min(#{})", expression);
  }

  public ByteArrayWindowFunctionOverStage over() {
    return new ByteArrayWindowFunctionOverStage(new ByteArrayWindowExpression(this));
  }

}
