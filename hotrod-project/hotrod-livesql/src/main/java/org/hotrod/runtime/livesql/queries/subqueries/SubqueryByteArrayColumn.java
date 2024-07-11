package org.hotrod.runtime.livesql.queries.subqueries;

import org.hotrod.runtime.livesql.exceptions.LiveSQLException;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.Helper;
import org.hotrod.runtime.livesql.expressions.binary.ByteArrayExpression;
import org.hotrod.runtime.livesql.queries.QueryWriter;

public class SubqueryByteArrayColumn extends ByteArrayExpression implements SubqueryColumn {

  // Properties

  private Subquery subquery;
  private String referencedColumnName;

  // Constructor

  public SubqueryByteArrayColumn(final Subquery subquery, final String referencedColumnName) {
    super(Expression.PRECEDENCE_COLUMN);
    this.subquery = subquery;
    this.referencedColumnName = referencedColumnName;
  }

  @Override
  public void captureTypeHandler() {
    Expression innerColumn = this.subquery.getSelect().findColumnWithName(this.referencedColumnName);
    if (innerColumn == null) {
      throw new LiveSQLException(
          "Could not find column '" + this.referencedColumnName + "' in subquery '" + this.subquery.getName() + "'.");
    }
    super.setTypeHandler(Helper.getTypeHandler(innerColumn));
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
