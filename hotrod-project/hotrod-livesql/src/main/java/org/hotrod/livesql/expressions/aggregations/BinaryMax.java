package org.hotrod.livesql.expressions.aggregations;

import org.hotrod.livesql.expressions.analytics.BinaryWindowExpression;
import org.hotrod.livesql.expressions.analytics.BinaryWindowFunctionOverStage;
import org.hotrod.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.livesql.expressions.binary.BinaryFunction;
import org.hotrod.livesql.expressions.binary.BinaryExpression;

public class BinaryMax extends BinaryFunction implements WindowableAggregationFunction {

  public BinaryMax(final BinaryExpression expression) {
    super("max(#{})", expression);
  }

  public BinaryWindowFunctionOverStage over() {
    return new BinaryWindowFunctionOverStage(new BinaryWindowExpression(this));
  }

}
