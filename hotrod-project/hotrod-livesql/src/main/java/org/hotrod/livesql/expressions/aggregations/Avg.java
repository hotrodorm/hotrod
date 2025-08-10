package org.hotrod.livesql.expressions.aggregations;

import org.hotrod.livesql.expressions.analytics.NumericWindowExpression;
import org.hotrod.livesql.expressions.analytics.NumericWindowFunctionOverStage;
import org.hotrod.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.livesql.expressions.numeric.NumericExpression;
import org.hotrod.livesql.expressions.numeric.NumericFunction;

public class Avg extends NumericFunction implements WindowableAggregationFunction {

  public Avg(final NumericExpression expression) {
    super("avg(#{})", expression);
  }

  public NumericWindowFunctionOverStage over() {
    return new NumericWindowFunctionOverStage(new NumericWindowExpression(this));
  }

}
