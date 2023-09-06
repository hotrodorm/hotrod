package org.hotrod.runtime.livesql.queries.select;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.ctes.CTE;

class PGSelect<R> extends AbstractSelect<R> {

  private boolean doNotAliasColumns;
  private List<ResultSetColumn> resultSetColumns = new ArrayList<>();

  PGSelect(final LiveSQLContext context, final List<CTE> ctes, final boolean distinct,
      final boolean doNotAliasColumns) {
    super(context, ctes, distinct);
    this.doNotAliasColumns = doNotAliasColumns;
  }

  // Setters

  void setResultSetColumns(final List<ResultSetColumn> resultSetColumns) {
    this.resultSetColumns = resultSetColumns;
  }

  // Rendering

  @Override
  protected void writeColumns(final QueryWriter w, final TableExpression baseTableExpression, final List<Join> joins) {
    super.writeExpandedColumns(w, baseTableExpression, joins, this.resultSetColumns, this.doNotAliasColumns);
  }

  @Override
  public List<ResultSetColumn> listColumns() {
    return this.resultSetColumns;
  }

}
