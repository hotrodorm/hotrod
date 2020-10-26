package org.hotrod.runtime.livesql.expressions.aggregations;

import org.hotrod.runtime.livesql.expressions.analytics.StringWindowExpression;
import org.hotrod.runtime.livesql.expressions.analytics.StringWindowFunctionOverStage;
import org.hotrod.runtime.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.runtime.livesql.expressions.strings.StringExpression;
import org.hotrod.runtime.livesql.expressions.strings.StringFunction;

public class StringMax extends StringFunction implements WindowableAggregationFunction {

  public StringMax(final StringExpression expression) {
    super("max(#{})", expression);
  }

  public StringWindowFunctionOverStage over() {
    return new StringWindowFunctionOverStage(new StringWindowExpression(this));
  }

}
