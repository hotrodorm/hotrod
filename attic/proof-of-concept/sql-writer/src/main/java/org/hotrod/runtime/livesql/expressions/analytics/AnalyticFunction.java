package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.QueryWriter;
import org.hotrod.runtime.livesql.expressions.Expression;

public abstract class AnalyticFunction<T> implements WindowableFunction<T> {

  private String functionName;
  protected Expression<?> expression;

  protected AnalyticFunction(final String functionName, final Expression<?> expression) {
    this.functionName = functionName;
    this.expression = expression;
  }

  @Override
  public WindowFunctionOverStage<T> over() {
    return new WindowFunctionOverStage<T>(new WindowExpression<T>(this));
  }

  // Rendering

  @Override
  public void renderBaseTo(final QueryWriter w) {
    renderHead(w);
    renderTail(w);
  }

  protected void renderHead(final QueryWriter w) {
    w.write(this.functionName);
    w.write("(");
    if (this.expression != null) {
      this.expression.renderTo(w);
    }
  }

  protected void renderTail(final QueryWriter w) {
    w.write(")");
  }

}
