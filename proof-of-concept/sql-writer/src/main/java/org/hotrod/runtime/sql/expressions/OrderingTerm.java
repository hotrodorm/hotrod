package org.hotrod.runtime.sql.expressions;

import org.hotrod.runtime.sql.QueryWriter;
import org.hotrod.runtime.sql.exceptions.InvalidSQLStatementException;

public class OrderingTerm {

  public static enum NullsOrdering {
    NULLS_FIRST("nulls first"), NULLS_LAST("nulls last");

    private String rendered;

    private NullsOrdering(final String rendered) {
      this.rendered = rendered;
    }

    String getRendered() {
      return rendered;
    }

  }

  private Expression<?> expression;
  private boolean ascending;
  private NullsOrdering nullsOrdering;

  public OrderingTerm(final Expression<?> expression, final boolean ascending) {
    if (expression == null) {
      throw new InvalidSQLStatementException(
          "Cannot use null value as column ordering. " + "Please speify a non null column in the ORDER BY clause");
    }
    this.expression = expression;
    this.ascending = ascending;
  }

  public OrderingTerm nullsFirst() {
    this.nullsOrdering = NullsOrdering.NULLS_FIRST;
    return this;
  }

  public OrderingTerm nullsLast() {
    this.nullsOrdering = NullsOrdering.NULLS_LAST;
    return this;
  }

  public Expression<?> getExpression() {
    return this.expression;
  }

  public boolean isAscending() {
    return ascending;
  };

  public void renderTo(final QueryWriter w) {
    this.expression.renderTo(w);
    if (!this.ascending) {
      w.write(" desc");
    }
    if (this.nullsOrdering != null) {
      w.write(" ");
      w.write(this.nullsOrdering.getRendered());
    }
  }

}
