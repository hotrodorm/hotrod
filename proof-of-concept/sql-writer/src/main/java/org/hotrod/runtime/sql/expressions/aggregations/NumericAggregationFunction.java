package org.hotrod.runtime.sql.expressions.aggregations;

import org.hotrod.runtime.sql.QueryWriter;
import org.hotrod.runtime.sql.expressions.Expression;
import org.hotrod.runtime.sql.expressions.analytics.WindowableAggregationFunction;

public abstract class NumericAggregationFunction extends WindowableAggregationFunction<Number> {

  private static final int PRECEDENCE = 2;

  private String functionName;
  private Expression<Number> expression;

  protected NumericAggregationFunction(final String functionName, final Expression<Number> expression) {
    super(PRECEDENCE);
    this.functionName = functionName;
    this.expression = expression;
  }

  @Override
  public final void renderTo(final QueryWriter w) {

    w.write(this.functionName);

    w.write("(");

    if (this.expression != null) {
      super.renderInner(this.expression, w);
    }

    w.write(")");

  }

}
