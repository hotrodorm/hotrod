package sql;

import java.util.List;

import sql.expressions.ResultSetColumn;
import sql.sqldialects.SQLDialect;

public class SelectSubquery extends AbstractSelect {

  private List<ReferenceableExpression> resultSetColumns = null;

  SelectSubquery(final SQLDialect sqlDialect) {
    super(sqlDialect);
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
