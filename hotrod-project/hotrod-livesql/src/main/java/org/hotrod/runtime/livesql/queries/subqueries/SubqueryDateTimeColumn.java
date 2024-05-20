package org.hotrod.runtime.livesql.queries.subqueries;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.datetime.DateTimeExpression;
import org.hotrod.runtime.livesql.queries.QueryWriter;

public class SubqueryDateTimeColumn extends DateTimeExpression {

  // Properties

  private Subquery subquery;
  private String columnName;

  // Constructor

  public SubqueryDateTimeColumn(final Subquery subquery, final String columnName) {
    super(Expression.PRECEDENCE_COLUMN);
    this.subquery = subquery;
    this.columnName = columnName;
  }

  // Rendering

  @Override
  public void renderTo(final QueryWriter w) {
    if (this.subquery.getName().isQuoted()) {
      w.write(w.getSQLDialect().quoteIdentifier(this.subquery.getName().getName()));
    } else {
      w.write(w.getSQLDialect()
          .canonicalToNatural(w.getSQLDialect().naturalToCanonical(this.subquery.getName().getName())));
    }
    w.write(".");
    w.write(w.getSQLDialect().canonicalToNatural(this.columnName));
  }

}
