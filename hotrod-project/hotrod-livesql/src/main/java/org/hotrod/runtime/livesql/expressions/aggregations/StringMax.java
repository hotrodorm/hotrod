package org.hotrod.runtime.livesql.expressions.aggregations;

import org.hotrod.runtime.livesql.expressions.strings.StringExpression;

public class StringMax extends StringAggregationFunction {

  public StringMax(final StringExpression expression) {
    super("max", expression);
  }

}
