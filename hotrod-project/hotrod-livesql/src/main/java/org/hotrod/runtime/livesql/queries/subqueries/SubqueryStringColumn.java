package org.hotrod.runtime.livesql.queries.subqueries;

import java.util.LinkedHashMap;
import java.util.logging.Logger;

import org.hotrod.runtime.livesql.exceptions.LiveSQLException;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.strings.StringExpression;
import org.hotrod.runtime.livesql.queries.QueryColumn;
import org.hotrod.runtime.livesql.queries.QueryWriter;
import org.hotrod.runtime.livesql.queries.select.sets.MHelper;

public class SubqueryStringColumn extends StringExpression implements SubqueryColumn {

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
  protected void computeQueryColumns() {
    log.info("subquery=" + subquery);
    log.info("subquery.getSelect()=" + subquery.getSelect());
    LinkedHashMap<String, QueryColumn> queryColumns = MHelper.getQueryColumns(subquery.getSelect());
    QueryColumn col = queryColumns.get(this.referencedColumnName);
    if (col == null) {
      throw new LiveSQLException("Referenced column '" + this.referencedColumnName + "' not found in subquery '"
          + this.subquery.getName() + "'");
    }
    log.info("--> col.getTypeHandler()=" + col.getTypeHandler());
    super.setTypeHandler(col.getTypeHandler());
  }

  @Override
  public String getReferencedColumnName() {
    return this.referencedColumnName;
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
    w.write(w.getSQLDialect().canonicalToNatural(this.referencedColumnName));
  }

}
