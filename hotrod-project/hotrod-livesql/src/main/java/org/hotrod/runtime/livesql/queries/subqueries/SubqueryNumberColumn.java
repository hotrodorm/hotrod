package org.hotrod.runtime.livesql.queries.subqueries;

import java.util.logging.Logger;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;
import org.hotrod.runtime.livesql.queries.QueryWriter;

public class SubqueryNumberColumn extends NumberExpression implements SubqueryColumn {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(SubqueryNumberColumn.class.getName());

  // Properties

  private Subquery subquery;
  private String referencedColumnName;

  // Constructor

  public SubqueryNumberColumn(final Subquery subquery, final String referencedColumnName) {
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
