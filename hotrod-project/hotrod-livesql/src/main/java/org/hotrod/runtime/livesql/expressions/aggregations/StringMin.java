package org.hotrod.runtime.livesql.expressions.aggregations;

import org.hotrod.runtime.livesql.expressions.analytics.StringWindowFunctionOverStage;
import org.hotrod.runtime.livesql.expressions.analytics.WindowExpression;
import org.hotrod.runtime.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.runtime.livesql.expressions.strings.StringExpression;
import org.hotrod.runtime.livesql.expressions.strings.StringFunction;

public class StringMin extends StringFunction implements WindowableAggregationFunction {

  public StringMin(final StringExpression expression) {
    super("min", expression);
  }

  public StringWindowFunctionOverStage over() {
    return new StringWindowFunctionOverStage(new WindowExpression(this));
  }

}
