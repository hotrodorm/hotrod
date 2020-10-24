package org.hotrod.runtime.livesql.expressions.aggregations;

import org.hotrod.runtime.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;
import org.hotrod.runtime.livesql.expressions.numbers.NumberFunction;

public class Sum extends NumberFunction implements WindowableAggregationFunction {

  public Sum(final NumberExpression expression) {
    super("sum", expression);
  }

}
