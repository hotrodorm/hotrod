package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.expressions.Expression;

public class Lead<T> extends PositionalAnalyticFunction<T> {

  public Lead(final Expression<?> expression, final Expression<Number> offset, final Expression<T> defaultValue) {
    super("lead", expression, offset, defaultValue);
  }

}
