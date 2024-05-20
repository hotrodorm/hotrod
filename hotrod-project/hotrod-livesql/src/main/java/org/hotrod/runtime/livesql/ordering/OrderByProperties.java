package org.hotrod.runtime.livesql.ordering;

import org.hotrod.runtime.livesql.exceptions.InvalidLiveSQLStatementException;
import org.hotrod.runtime.livesql.expressions.ComparableExpression;
import org.hotrod.runtime.livesql.queries.QueryWriter;
import org.hotrodorm.hotrod.utils.SUtil;

public class OrderByProperties {

  public static enum NullsOrdering {

    NULLS_FIRST("NULLS FIRST"), //
    NULLS_LAST("NULLS LAST");

    private String rendered;

    private NullsOrdering(final String rendered) {
      this.rendered = rendered;
    }

    String getRendered() {
      return rendered;
    }

  }

  private ComparableExpression expression; // a.name, a.qty * a.price desc
  private String alias; // "total" desc
  private Integer ordinal; // 1 desc

  private boolean ascending;
  private NullsOrdering nullsOrdering;

  public OrderByProperties(final ComparableExpression expression, final boolean ascending) {
    if (expression == null) {
      throw new InvalidLiveSQLStatementException(
          "Cannot use null value as column ordering. " + "Please specify a non null column in the ORDER BY clause");
    }
    this.expression = expression;
    this.alias = null;
    this.ordinal = null;
    this.ascending = ascending;
    this.nullsOrdering = null;
  }

  public OrderByProperties(final String alias, final boolean ascending) {
    if (alias == null || SUtil.isEmpty(alias)) {
      throw new InvalidLiveSQLStatementException("Cannot use empty column alias for ordering. "
          + "Please specify a non-empty column alias in the ORDER BY clause");
    }
    this.expression = null;
    this.alias = alias;
    this.ordinal = null;
    this.ascending = ascending;
    this.nullsOrdering = null;
  }

  public OrderByProperties(final int ordinal, final boolean ascending) {
    if (ordinal < 1) {
      throw new InvalidLiveSQLStatementException("Cannot use an ordinal with value less than 1 for ordering. "
          + "Please specify an ordinal greater or equal to 1 in the ORDER BY clause");
    }
    this.expression = null;
    this.alias = null;
    this.ordinal = ordinal;
    this.ascending = ascending;
    this.nullsOrdering = null;
  }

  void setNullsOrdering(final NullsOrdering nullsOrdering) {
    this.nullsOrdering = nullsOrdering;
  }

  public void renderTo(final QueryWriter w) {

    // expression, alias, or ordinal

    if (this.expression != null) {
      this.expression.renderTo(w);
    } else if (this.alias != null) {
      w.write(w.getSQLDialect().canonicalToNatural(this.alias));
    } else {
      w.write("" + this.ordinal);
    }

    // ascending or descending

    if (!this.ascending) {
      w.write(" DESC");
    }

    // nulls first or nulls last

    if (this.nullsOrdering != null) {
      w.write(" ");
      w.write(this.nullsOrdering.getRendered());
    }
  }

}
