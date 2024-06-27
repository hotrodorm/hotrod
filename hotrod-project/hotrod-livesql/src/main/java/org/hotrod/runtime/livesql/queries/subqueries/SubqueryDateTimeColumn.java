package org.hotrod.runtime.livesql.queries.subqueries;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.datetime.DateTimeExpression;
import org.hotrod.runtime.livesql.queries.QueryWriter;

public class SubqueryDateTimeColumn extends DateTimeExpression implements SubqueryColumn {

  // Properties

  private Subquery subquery;
  private String referencedColumnName;

  // Constructor

  public SubqueryDateTimeColumn(final Subquery subquery, final String referencedColumnName) {
    super(Expression.PRECEDENCE_COLUMN);
    this.subquery = subquery;
    this.referencedColumnName = referencedColumnName;
  }

  @Override
  public String getReferencedColumnName() {
    return this.referencedColumnName;
  }

  // Rendering

  @Override
  protected void renderTo(final QueryWriter w) {
    this.subquery.getName().renderTo(w);
    w.write(".");
    w.write(w.getSQLDialect().canonicalToNatural(this.referencedColumnName));
  }

}
