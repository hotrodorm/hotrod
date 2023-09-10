package org.hotrod.runtime.livesql.queries.ctes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.hotrod.runtime.livesql.dialects.LiveSQLDialect;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.queries.select.ExecutableSelect;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;
import org.hotrod.runtime.livesql.queries.subqueries.Subquery;
import org.hotrod.runtime.livesql.util.SubqueryUtil;

@SuppressWarnings("deprecation")
public class CTE extends Subquery {

  protected CTE(String name, final String[] columns) {
    super(name, columns);
  }

  public CTE(String name, ExecutableSelect<?> select) {
    super(name, null, select);
  }

  public CTE(String name, String[] columns, ExecutableSelect<?> select) {
    super(name, columns, select);
  }

  public boolean isRecursive() {
    return false;
  }

  // Rendering

  @Override
  public void renderTo(QueryWriter w) {
    w.write(w.getSQLDialect().canonicalToNatural(w.getSQLDialect().naturalToCanonical(super.getName())));
  }

  public void renderDefinitionTo(QueryWriter w, LiveSQLDialect dialect) {
    w.write(w.getSQLDialect().canonicalToNatural(w.getSQLDialect().naturalToCanonical(super.getName())));
    if (this.columns != null && this.columns.length > 0) {
      w.write(" (");
      w.write(Arrays.stream(this.columns).map(a -> w.getSQLDialect().canonicalToNatural(a))
          .collect(Collectors.joining(", ")));
      w.write(")");
    }
    w.enterLevel();
    w.write(" as (\n");
    super.getSelect().renderTo(w);
    w.exitLevel();
    w.write("\n");
    w.write(")");
  }

  public List<ResultSetColumn> getColumns() throws IllegalAccessException {
    if (this.columns != null && this.columns.length > 0) {
      List<ResultSetColumn> cols = super.getSelect().listColumns();
      if (this.columns.length != cols.size()) {
        throw new RuntimeException("The number of columns (" + cols.size() + ") in the CTE '" + super.getName()
            + "' differs from the number of declared column names for it (" + this.columns.length + ").");
      }
      List<ResultSetColumn> fcols = new ArrayList<>();
      for (int i = 0; i < this.columns.length; i++) {
        ResultSetColumn rsc = cols.get(i);
        Expression col = SubqueryUtil.castSubqueryColumnAsExternalLevelSubqueryColumn(this, rsc, this.columns[i]);
        fcols.add(col);
      }
      return fcols;
    } else {
      return super.getColumns();
    }
  }

}
