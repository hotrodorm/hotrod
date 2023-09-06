package org.hotrod.runtime.livesql.queries.select;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.queries.ctes.CTE;

class PGSelectObject<R> extends AbstractSelectObject<R> {

  private boolean doNotAliasColumns;
  private List<ResultSetColumn> resultSetColumns = new ArrayList<>();

  PGSelectObject(final List<CTE> ctes, final boolean distinct, final boolean doNotAliasColumns) {
    super(ctes, distinct);
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
