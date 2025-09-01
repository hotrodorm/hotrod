package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.QueryWriter;
import org.hotrod.runtime.livesql.expressions.Expression;

public abstract class PositionalAnalyticFunction<T> extends AnalyticFunction<T> {

  private Expression<Number> offset;
  private Expression<T> defaultValue;

  protected PositionalAnalyticFunction(final String functionName, final Expression<?> expression,
      final Expression<Number> offset, final Expression<T> defaultValue) {
    super(functionName, expression);
    this.offset = offset;
    this.defaultValue = defaultValue;
  }

  // Rendering

  @Override
  public void renderBaseTo(final QueryWriter w) {
    super.renderHead(w);
    if (this.offset != null) {
      this.offset.renderTo(w);
      if (this.defaultValue != null) {
        this.defaultValue.renderTo(w);
      }
    }
    super.renderTail(w);
  }

}
