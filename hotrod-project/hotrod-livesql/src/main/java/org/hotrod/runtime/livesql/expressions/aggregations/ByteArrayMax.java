package org.hotrod.runtime.livesql.expressions.aggregations;

import org.hotrod.runtime.livesql.expressions.analytics.ByteArrayWindowFunctionOverStage;
import org.hotrod.runtime.livesql.expressions.analytics.WindowExpression;
import org.hotrod.runtime.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.runtime.livesql.expressions.binary.ByteArrayExpression;
import org.hotrod.runtime.livesql.expressions.binary.ByteArrayFunction;

public class ByteArrayMax extends ByteArrayFunction implements WindowableAggregationFunction {

  public ByteArrayMax(final ByteArrayExpression expression) {
    super("max", expression);
  }

  public ByteArrayWindowFunctionOverStage over() {
    return new ByteArrayWindowFunctionOverStage(new WindowExpression(this));
  }

}
