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

public class CTE extends Subquery {

  protected String[] aliases;

  public CTE(String name, ExecutableSelect<?> select) {
    super(name, select);
    this.aliases = null;
  }

  public CTE(String name, String[] aliases, ExecutableSelect<?> select) {
    super(name, select);
    this.aliases = aliases;
  }

  public boolean isRecursive() {
    return false;
  }

  // Rendering

  @Override
  public void renderTo(QueryWriter w, LiveSQLDialect dialect) {
    w.write(w.getSqlDialect().canonicalToNatural(w.getSqlDialect().naturalToCanonical(super.getAlias())));
  }

  public void renderDefinitionTo(QueryWriter w, LiveSQLDialect dialect) {
    w.write(w.getSqlDialect().canonicalToNatural(w.getSqlDialect().naturalToCanonical(super.getAlias())));
    if (this.aliases != null && this.aliases.length > 0) {
      w.write(" (");
      w.write(Arrays.stream(this.aliases).map(a -> w.getSqlDialect().canonicalToNatural(a))
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
    if (this.aliases != null && this.aliases.length > 0) {
      List<ResultSetColumn> cols = super.getSelect().listColumns();
      if (this.aliases.length != cols.size()) {
        throw new RuntimeException("The number of columns (" + cols.size() + ") in the CTE '" + super.getAlias()
            + "' differs from the number of declared column names for it (" + this.aliases.length + ").");
      }
      List<ResultSetColumn> fcols = new ArrayList<>();
      for (int i = 0; i < this.aliases.length; i++) {
        ResultSetColumn rsc = cols.get(i);
        Expression col = SubqueryUtil.castSubqueryColumnAsExternalLevelSubqueryColumn(this, rsc, this.aliases[i]);
        fcols.add(col);
      }
      return fcols;
    } else {
      return super.getColumns();
    }
  }

}
