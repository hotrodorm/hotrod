package org.hotrod.runtime.sql.expressions.analytics;

import org.hotrod.runtime.sql.expressions.Expression;

public class Lag<T> extends PositionalAnalyticFunction<T> {

  public Lag(final Expression<?> expression, final Expression<Number> offset, final Expression<T> defaultValue) {
    super("lag", expression, offset, defaultValue);
  }

}
