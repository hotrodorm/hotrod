package org.hotrod.runtime.sql.expressions.analytics;

import org.hotrod.runtime.sql.expressions.Expression;

public class Lead<T> extends PositionalAnalyticFunction<T> {

  public Lead(final Expression<?> expression, final Expression<Number> offset, final Expression<T> defaultValue) {
    super("lead", expression, offset, defaultValue);
  }

}
