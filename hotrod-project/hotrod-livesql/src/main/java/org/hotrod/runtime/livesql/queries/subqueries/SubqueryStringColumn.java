package org.hotrod.runtime.livesql.queries.subqueries;

import java.util.LinkedHashMap;

import org.hotrod.runtime.livesql.exceptions.LiveSQLException;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.strings.StringExpression;
import org.hotrod.runtime.livesql.queries.QueryColumn;
import org.hotrod.runtime.livesql.queries.QueryWriter;
import org.hotrod.runtime.livesql.queries.select.sets.MHelper;

public class SubqueryStringColumn extends StringExpression {

  // Properties

  private Subquery subquery;
  private String columnName;

  // Constructor

  public SubqueryStringColumn(final Subquery subquery, final String columnName) {
    super(Expression.PRECEDENCE_COLUMN);
    this.subquery = subquery;
    this.columnName = columnName;

    LinkedHashMap<String, QueryColumn> queryColumns = MHelper.getQueryColumns(subquery.getSelect());
    QueryColumn col = queryColumns.get(columnName);
    if (col == null) {
      throw new LiveSQLException(
          "Referenced column '" + columnName + "' not found in subquery '" + this.subquery.getName() + "'");
    }
    super.setTypeHandler(col.getTypeHandler());

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
