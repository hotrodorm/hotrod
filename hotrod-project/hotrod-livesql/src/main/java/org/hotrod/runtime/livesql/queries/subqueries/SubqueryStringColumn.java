package org.hotrod.runtime.livesql.queries.subqueries;

import java.util.logging.Logger;

import org.hotrod.runtime.livesql.exceptions.LiveSQLException;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.Helper;
import org.hotrod.runtime.livesql.expressions.strings.StringExpression;
import org.hotrod.runtime.livesql.queries.QueryWriter;

public class SubqueryStringColumn extends StringExpression implements SubqueryColumn {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(SubqueryStringColumn.class.getName());

  // Properties

  private Subquery subquery;
  private String referencedColumnName;

  // Constructor

  public SubqueryStringColumn(final Subquery subquery, final String referencedColumnName) {
    super(Expression.PRECEDENCE_COLUMN);
    this.subquery = subquery;
    this.referencedColumnName = referencedColumnName;
  }

  @Override
  public String getReferencedColumnName() {
    return this.referencedColumnName;
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

  // Rendering

  @Override
  protected void renderTo(final QueryWriter w) {
    this.subquery.getName().renderTo(w);
    w.write(".");
    w.write(w.getSQLDialect().canonicalToNatural(this.referencedColumnName));
  }

  public String toString() {
    return this.subquery.getName().toString() + ":" + this.referencedColumnName + " typeHandler="
        + super.getTypeHandler();
  }

}
