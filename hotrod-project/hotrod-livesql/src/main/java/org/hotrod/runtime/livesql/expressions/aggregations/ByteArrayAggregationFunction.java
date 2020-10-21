package org.hotrod.runtime.livesql.expressions.aggregations;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.analytics.WindowableAggregationFunction;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public abstract class ByteArrayAggregationFunction extends WindowableAggregationFunction<byte[]> {

  private String functionName;
  private Expression<byte[]> expression;

  protected ByteArrayAggregationFunction(final String functionName, final Expression<byte[]> expression) {
    super(Expression.PRECEDENCE_FUNCTION);
    this.functionName = functionName;
    this.expression = expression;
    super.register(this.expression);
  }

  @Override
  public void renderTo(final QueryWriter w) {
    renderHead(w);
    renderTail(w);
  }

  private void renderHead(final QueryWriter w) {
    w.write(this.functionName);
    w.write("(");
    if (this.expression != null) {
      this.expression.renderTo(w);
    }
  }

  private void renderTail(final QueryWriter w) {
    w.write(")");
  }

}
