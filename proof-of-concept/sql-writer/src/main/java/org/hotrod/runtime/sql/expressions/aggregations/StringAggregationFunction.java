package org.hotrod.runtime.sql.expressions.aggregations;

import org.hotrod.runtime.sql.QueryWriter;
import org.hotrod.runtime.sql.expressions.Expression;
import org.hotrod.runtime.sql.expressions.analytics.WindowableAggregationFunction;

public abstract class StringAggregationFunction extends WindowableAggregationFunction<String> {

  private static final int PRECEDENCE = 2;

  private String functionName;
  private Expression<String> expression;

  protected StringAggregationFunction(final String functionName, final Expression<String> expression) {
    super(PRECEDENCE);
    this.functionName = functionName;
    this.expression = expression;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    renderHead(w);
    renderTail(w);
  }

  protected void renderTail(final QueryWriter w) {
    w.write(")");
  }

  protected void renderHead(final QueryWriter w) {
    w.write(this.functionName);
    w.write("(");
    this.expression.renderTo(w);
  }

  // Literal

//  static class LiteralString extends Expression<String> {
//
//    private static final int PRECEDENCE = 1;
//
//    private String verbatim;
//
//    protected LiteralString(final String verbatim) {
//      super(PRECEDENCE);
//      this.verbatim = verbatim;
//    }
//
//    @Override
//    public void renderTo(final QueryWriter w) {
//      w.write(this.verbatim);
//    }
//
//  }

}
