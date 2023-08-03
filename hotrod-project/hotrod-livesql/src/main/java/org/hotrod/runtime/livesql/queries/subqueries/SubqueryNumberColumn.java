package org.hotrod.runtime.livesql.queries.subqueries;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class SubqueryNumberColumn extends NumberExpression {

  // Properties

  private Subquery<?> subquery;
  private String columnName;

  // Constructor

  public SubqueryNumberColumn(final Subquery<?> subquery, final String columnName) {
    super(Expression.PRECEDENCE_COLUMN);
    this.subquery = subquery;
    this.columnName = columnName;
  }

  // Rendering

  @Override
  public void renderTo(final QueryWriter w) {
    w.write(w.getSqlDialect().canonicalToNatural(w.getSqlDialect().naturalToCanonical(this.subquery.getAlias())));
    w.write(".");
    w.write(w.getSqlDialect().canonicalToNatural(this.columnName));
  }

}
