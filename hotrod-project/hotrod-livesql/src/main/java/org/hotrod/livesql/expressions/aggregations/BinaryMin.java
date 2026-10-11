package org.hotrod.livesql.expressions.aggregations;

import org.hotrod.livesql.expressions.analytics.BinaryWindowExpression;
import org.hotrod.livesql.expressions.analytics.BinaryWindowFunctionOverStage;
import org.hotrod.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.livesql.expressions.binary.BinaryExpression;

public class BinaryMin extends BinaryUnfilteredFunction implements WindowableAggregationFunction {

  public BinaryMin(final BinaryExpression expression) {
    super("min", expression);
  }

  public BinaryWindowFunctionOverStage over() {
    return new BinaryWindowFunctionOverStage(new BinaryWindowExpression(this));
  }

}
