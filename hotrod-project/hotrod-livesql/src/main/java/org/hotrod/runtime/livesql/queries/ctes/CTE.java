package org.hotrod.runtime.livesql.queries.ctes;

import org.hotrod.runtime.livesql.Row;
import org.hotrod.runtime.livesql.dialects.LiveSQLDialect;
import org.hotrod.runtime.livesql.queries.select.ExecutableSelect;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;
import org.hotrod.runtime.livesql.queries.subqueries.Subquery;

public class CTE extends Subquery<Row> {

  public CTE(String alias, ExecutableSelect<Row> select) {
    super(alias, select);
  }

  // Rendering

  @Override
  public void renderTo(QueryWriter w, LiveSQLDialect dialect) {
    w.write(w.getSqlDialect().canonicalToNatural(w.getSqlDialect().naturalToCanonical(super.getAlias())));
  }

  public void renderDefinitionTo(QueryWriter w, LiveSQLDialect dialect) {
    w.write(w.getSqlDialect().canonicalToNatural(w.getSqlDialect().naturalToCanonical(super.getAlias())));
    w.enterLevel();
    w.write(" as (\n");
    super.getSelect().renderTo(w);
    w.exitLevel();
    w.write("\n");
    w.write(")");
  }

}
