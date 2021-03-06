package org.hotrod.runtime.livesql.expressions.aggregations;

import org.hotrod.runtime.livesql.QueryWriter;
import org.hotrod.runtime.livesql.expressions.Expression;

public abstract class NonWindowableAggregationFunction<T> extends AggregationFunction<T> {

  private static final int PRECEDENCE = 1;

  private String functionName;
  private String qualifier;
  protected Expression<?> expression;

  public NonWindowableAggregationFunction(final String functionName, final String qualifier,
      final Expression<?> expression) {
    super(PRECEDENCE);
    if (qualifier != null && expression == null) {
      throw new IllegalArgumentException("Invalid parameters on function '" + functionName + "': cannot specify '"
          + qualifier + "' clause without any expression");
    }
    this.functionName = functionName;
    this.qualifier = qualifier;
    this.expression = expression;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    renderHead(w);
    renderTail(w);
  }

  protected void renderHead(final QueryWriter w) {
    w.write(this.functionName);

    w.write("(");

    if (this.qualifier != null) {
      w.write(this.qualifier);
    }

    if (this.qualifier != null && this.expression != null) {
      w.write(" ");
    }

    if (this.expression != null) {
      super.renderInner(this.expression, w);
    }
  }

  protected void renderTail(final QueryWriter w) {
    w.write(")");
  }

}
