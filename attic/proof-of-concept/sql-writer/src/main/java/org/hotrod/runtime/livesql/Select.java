package org.hotrod.runtime.livesql;

import java.util.List;

import org.hotrod.runtime.livesql.dialects.SQLDialect;
import org.hotrod.runtime.livesql.expressions.ResultSetColumn;

class Select extends AbstractSelect {

  private List<ResultSetColumn> resultSetColumns = null;

  Select(final SQLDialect sqlDialect, final boolean distinct) {
    super(sqlDialect, distinct);
  }

  // Setters

  void setResultSetColumns(final List<ResultSetColumn> resultSetColumns) {
    this.resultSetColumns = resultSetColumns;
  }

  // Rendering

  @Override
  protected void writeColumns(final QueryWriter w) {
    if (this.resultSetColumns == null || this.resultSetColumns.isEmpty()) {
      w.write("\n  *");
    } else {
      boolean first = true;
      for (ResultSetColumn c : this.resultSetColumns) {
        if (first) {
          first = false;
        } else {
          w.write(",");
        }
        w.write("\n  ");
        c.renderTo(w);
      }
    }
  }

}
