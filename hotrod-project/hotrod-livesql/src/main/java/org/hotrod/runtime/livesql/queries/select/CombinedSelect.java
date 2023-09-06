package org.hotrod.runtime.livesql.queries.select;

import java.util.List;
import java.util.stream.Collectors;

import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.ctes.CTE;

class CombinedSelect<T> extends AbstractSelect<T> {

  private List<ResultSetColumn> resultSetColumns = null;

  CombinedSelect(final LiveSQLContext context, final List<CTE> ctes, final boolean distinct) {
    super(context, ctes, distinct);
  }

  // Setters

  void setResultSetColumns(final List<ResultSetColumn> resultSetColumns) {
    this.resultSetColumns = resultSetColumns;
  }

  // Rendering

  @Override
  protected void writeColumns(final QueryWriter w, final TableExpression baseTableExpression, final List<Join> joins) {
    super.writeExpandedColumns(w, baseTableExpression, joins,
        this.resultSetColumns.stream().collect(Collectors.toList()), true);
  }

  @Override
  public List<ResultSetColumn> listColumns() {
    return this.resultSetColumns;
  }

}
