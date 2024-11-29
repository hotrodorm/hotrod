package org.hotrod.runtime.livesql.expressions.aggregations;

import org.hotrod.runtime.livesql.expressions.analytics.ByteArrayWindowExpression;
import org.hotrod.runtime.livesql.expressions.analytics.ByteArrayWindowFunctionOverStage;
import org.hotrod.runtime.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.runtime.livesql.expressions.binary.ByteArrayFunction;
import org.hotrod.runtime.livesql.expressions.binary.GeneralByteArrayExpression;

public class ByteArrayMax extends ByteArrayFunction implements WindowableAggregationFunction {

  public ByteArrayMax(final GeneralByteArrayExpression expression) {
    super("max(#{})", expression);
  }

  public ByteArrayWindowFunctionOverStage over() {
    return new ByteArrayWindowFunctionOverStage(new ByteArrayWindowExpression(this));
  }

}
