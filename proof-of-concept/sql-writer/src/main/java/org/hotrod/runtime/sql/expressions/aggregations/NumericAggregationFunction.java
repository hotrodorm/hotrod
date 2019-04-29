package org.hotrod.runtime.sql.expressions.aggregations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hotrod.runtime.sql.QueryWriter;
import org.hotrod.runtime.sql.exceptions.InvalidSQLStatementException;
import org.hotrod.runtime.sql.expressions.Expression;

public abstract class NumericAggregationFunction extends Expression<Number> {

  private static final int PRECEDENCE = 2;

  private String function;
  private String decorator;
  private List<Expression<?>> expressions;

  protected NumericAggregationFunction(final String function, final String decorator,
      final List<Expression<?>> expressions) {
    super(PRECEDENCE);
    this.function = function;
    this.decorator = decorator;
    this.expressions = expressions;
  }

  @Override
  public final void renderTo(final QueryWriter w) {

    w.write(this.function);

    w.write("(");

    if (this.decorator != null) {
      w.write(this.decorator);
      w.write(" ");
    }

    boolean first = true;
    for (Expression<?> expr : this.expressions) {
      if (first) {
        first = false;
      } else {
        w.write(", ");
      }
      super.renderInner(expr, w);
    }

    w.write(")");

  }

  // Utilities

  @SafeVarargs
  public static List<Expression<?>> toNonEmptyList(final String errorMessage, final Expression<?>... expressions) {
    if (expressions == null || expressions.length == 0) {
      throw new InvalidSQLStatementException(errorMessage);
    }
    return Arrays.asList(expressions);
  }

  public static List<Expression<?>> toList(final Expression<?> expression) {
    List<Expression<?>> l = new ArrayList<Expression<?>>();
    l.add(expression);
    return l;
  }

  // Literal

  static class LiteralString extends Expression<String> {

    private static final int PRECEDENCE = 1;

    private String verbatim;

    protected LiteralString(final String verbatim) {
      super(PRECEDENCE);
      this.verbatim = verbatim;
    }

    @Override
    public void renderTo(final QueryWriter w) {
      w.write(this.verbatim);
    }

  }

}
