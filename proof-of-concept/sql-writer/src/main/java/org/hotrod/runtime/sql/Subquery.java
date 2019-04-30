package org.hotrod.runtime.sql;

import java.util.List;

import org.hotrod.runtime.sql.dialects.SQLDialect;
import org.hotrod.runtime.sql.expressions.ResultSetColumn;

public class Subquery extends AbstractSelect {

  private List<ReferenceableExpression> resultSetColumns = null;

  Subquery(final SQLDialect sqlDialect, final boolean distinct) {
    super(sqlDialect, distinct);
  }

  // Setters

  void setResultSetColumns(final List<ReferenceableExpression> resultSetColumns) {
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
