package org.hotrod.runtime.livesql.queries.ctes;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.hotrod.runtime.livesql.Row;
import org.hotrod.runtime.livesql.dialects.LiveSQLDialect;
import org.hotrod.runtime.livesql.queries.select.ExecutableSelect;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;
import org.hotrod.runtime.livesql.queries.subqueries.Subquery;

public class CTE extends Subquery<Row> {

  private String[] columnNames;

  public CTE(String name, ExecutableSelect<Row> select) {
    super(name, select);
    this.columnNames = null;
  }

  public CTE(String name, String[] columnNames, ExecutableSelect<Row> select) {
    super(name, select);
    this.columnNames = columnNames;
  }

  // Rendering

  @Override
  public void renderTo(QueryWriter w, LiveSQLDialect dialect) {
    w.write(w.getSqlDialect().canonicalToNatural(w.getSqlDialect().naturalToCanonical(super.getAlias())));
  }

  public void renderDefinitionTo(QueryWriter w, LiveSQLDialect dialect) {
    w.write(w.getSqlDialect().canonicalToNatural(w.getSqlDialect().naturalToCanonical(super.getAlias())));
    if (this.columnNames != null && this.columnNames.length > 0) {
      w.write(" (");
      w.write(Arrays.stream(this.columnNames)
          .map(a -> w.getSqlDialect().canonicalToNatural(w.getSqlDialect().naturalToCanonical(a)))
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

}
