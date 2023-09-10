package org.hotrod.runtime.livesql.queries.subqueries;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.binary.ByteArrayExpression;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class SubqueryByteArrayColumn extends ByteArrayExpression {

  // Properties

  private Subquery subquery;
  private String columnName;

  // Constructor

  public SubqueryByteArrayColumn(final Subquery subquery, final String columnName) {
    super(Expression.PRECEDENCE_COLUMN);
    this.subquery = subquery;
    this.columnName = columnName;
  }

  // Rendering

  @Override
  public void renderTo(final QueryWriter w) {
    w.write(w.getSQLDialect().canonicalToNatural(w.getSQLDialect().naturalToCanonical(this.subquery.getName())));
    w.write(".");
    w.write(w.getSQLDialect().canonicalToNatural(this.columnName));
  }

}
