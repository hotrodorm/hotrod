package org.hotrod.runtime.livesql.queries.subqueries;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.queries.QueryWriter;

public class SubqueryBooleanColumn extends Predicate implements SubqueryColumn {

  // Properties

  private Subquery subquery;
  private String referencedColumnName;

  // Constructor

  public SubqueryBooleanColumn(final Subquery subquery, final String referencedColumnName) {
    super(Expression.PRECEDENCE_COLUMN);
    this.subquery = subquery;
    this.referencedColumnName = referencedColumnName;
  }

//  @Override
//  protected void computeQueryColumns() {
//    LinkedHashMap<String, QueryColumn> queryColumns = MHelper.getQueryColumns(subquery.getSelect());
//    QueryColumn col = queryColumns.get(this.referencedColumnName);
//    if (col == null) {
//      throw new LiveSQLException("Referenced column '" + this.referencedColumnName + "' not found in subquery '"
//          + this.subquery.getName() + "'");
//    }
//    super.setTypeHandler(col.getTypeHandler());
//  }

  @Override
  public String getReferencedColumnName() {
    return this.referencedColumnName;
  }

  // Rendering

  @Override
  protected void renderTo(final QueryWriter w) {
    if (this.subquery.getName().isQuoted()) {
      w.write(w.getSQLDialect().quoteIdentifier(this.subquery.getName().getName()));
    } else {
      w.write(w.getSQLDialect()
          .canonicalToNatural(w.getSQLDialect().naturalToCanonical(this.subquery.getName().getName())));
    }
    w.write(".");
    w.write(w.getSQLDialect().canonicalToNatural(this.referencedColumnName));
  }

}
