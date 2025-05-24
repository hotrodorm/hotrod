package org.hotrod.livesql.expressions.aggregations;

import org.hotrod.livesql.expressions.analytics.ByteArrayWindowExpression;
import org.hotrod.livesql.expressions.analytics.ByteArrayWindowFunctionOverStage;
import org.hotrod.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.livesql.expressions.binary.ByteArrayFunction;
import org.hotrod.livesql.expressions.binary.GeneralByteArrayExpression;

public class ByteArrayMax extends ByteArrayFunction implements WindowableAggregationFunction {

  public ByteArrayMax(final GeneralByteArrayExpression expression) {
    super("max(#{})", expression);
  }

  public ByteArrayWindowFunctionOverStage over() {
    return new ByteArrayWindowFunctionOverStage(new ByteArrayWindowExpression(this));
  }

}
