package org.hotrod.livesql.expressions.aggregations;

import org.hotrod.livesql.expressions.analytics.BinaryWindowExpression;
import org.hotrod.livesql.expressions.analytics.BinaryWindowFunctionOverStage;
import org.hotrod.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.livesql.expressions.binary.BinaryFunction;
import org.hotrod.livesql.expressions.binary.GeneralBinaryExpression;

public class BinaryMin extends BinaryFunction implements WindowableAggregationFunction {

  public BinaryMin(final GeneralBinaryExpression expression) {
    super("min(#{})", expression);
  }

  public BinaryWindowFunctionOverStage over() {
    return new BinaryWindowFunctionOverStage(new BinaryWindowExpression(this));
  }

}
