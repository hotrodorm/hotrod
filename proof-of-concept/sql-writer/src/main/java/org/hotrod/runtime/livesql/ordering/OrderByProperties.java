package org.hotrod.runtime.livesql.ordering;

import org.hotrod.runtime.livesql.QueryWriter;
import org.hotrod.runtime.livesql.exceptions.InvalidSQLStatementException;
import org.hotrod.runtime.livesql.expressions.Expression;

public class OrderByProperties {

  public static enum NullsOrdering {

    NULLS_FIRST("nulls first"), //
    NULLS_LAST("nulls last");

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

  public OrderByProperties(final Expression<?> expression, final boolean ascending) {
    if (expression == null) {
      throw new InvalidSQLStatementException(
          "Cannot use null value as column ordering. " + "Please speify a non null column in the ORDER BY clause");
    }
    this.expression = expression;
    this.ascending = ascending;
    this.nullsOrdering = null;
  }

  void setNullsOrdering(final NullsOrdering nullsOrdering) {
    this.nullsOrdering = nullsOrdering;
  }

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
