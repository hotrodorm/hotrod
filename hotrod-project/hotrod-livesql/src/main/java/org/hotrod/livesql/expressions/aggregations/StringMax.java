package org.hotrod.livesql.expressions.aggregations;

import org.hotrod.livesql.expressions.analytics.StringWindowExpression;
import org.hotrod.livesql.expressions.analytics.StringWindowFunctionOverStage;
import org.hotrod.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.livesql.expressions.strings.GeneralStringExpression;
import org.hotrod.livesql.expressions.strings.StringFunction;

public class StringMax extends StringFunction implements WindowableAggregationFunction {

  public StringMax(final GeneralStringExpression expression) {
    super("max(#{})", expression);
  }

  public StringWindowFunctionOverStage over() {
    return new StringWindowFunctionOverStage(new StringWindowExpression(this));
  }

}
