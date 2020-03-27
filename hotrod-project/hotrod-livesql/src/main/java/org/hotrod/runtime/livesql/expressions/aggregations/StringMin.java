package org.hotrod.runtime.livesql.expressions.aggregations;

import org.hotrod.runtime.livesql.expressions.strings.StringExpression;

public class StringMin extends StringAggregationFunction {

  public StringMin(final StringExpression expression) {
    super("min", expression);
  }

}
