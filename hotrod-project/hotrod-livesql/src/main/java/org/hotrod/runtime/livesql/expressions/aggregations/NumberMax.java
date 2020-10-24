package org.hotrod.runtime.livesql.expressions.aggregations;

import org.hotrod.runtime.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;
import org.hotrod.runtime.livesql.expressions.numbers.NumberFunction;

public class NumberMax extends NumberFunction implements WindowableAggregationFunction {

  public NumberMax(final NumberExpression expression) {
    super("max", expression);
  }

}
