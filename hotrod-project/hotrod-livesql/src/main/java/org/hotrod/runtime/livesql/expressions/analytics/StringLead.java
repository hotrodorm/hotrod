package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;
import org.hotrod.runtime.livesql.expressions.strings.StringExpression;
import org.hotrod.runtime.livesql.expressions.strings.StringFunction;

public class StringLead extends StringFunction implements PositionalAnalyticFunction {

  public StringLead(final StringExpression expression, final NumberExpression offset,
      final StringExpression defaultValue) {
    super("lead", expression, offset, defaultValue);
  }

}
