package org.hotrod.livesql.queries.subqueries;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.datetime.DateTimeExpression;
import org.hotrod.livesql.queries.QueryWriter;

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
  protected String getReferenceName() {
    return this.referencedColumnName;
  }

  @Override
  public final String getProperty() {
    return this.referencedColumnName;
  }

  // Rendering

  @Override
  protected void renderTo(final QueryWriter w) {
    this.subquery.getName().renderTo(w);
    w.write(".");
    w.write(w.getSQLDialect().canonicalToNatural(this.referencedColumnName));
  }

  protected String render() {
    return this.subquery.getName().toString() + ":" + this.referencedColumnName + " typeHandler="
        + super.getTypeHandler();
  }

}
