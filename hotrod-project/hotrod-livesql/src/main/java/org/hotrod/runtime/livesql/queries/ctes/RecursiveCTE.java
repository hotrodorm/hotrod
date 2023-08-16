package org.hotrod.runtime.livesql.queries.ctes;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.hotrod.runtime.livesql.dialects.LiveSQLDialect;
import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.queries.select.ExecutableSelect;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class RecursiveCTE extends CTE {

  // Properties

  private String[] aliases;

  private ExecutableSelect<?> anchorTerm;
  private boolean unionAll;
  private ExecutableSelect<?> recursiveTerm;

  // Constructor

  public RecursiveCTE(final String name, final String[] aliases) {
    super(name, null);
    this.aliases = aliases;
  }

  public void as(final ExecutableSelect<?> anchorTerm, final ExecutableSelect<?> recursiveTerm) {
    this.anchorTerm = anchorTerm;
    this.unionAll = true;
    this.recursiveTerm = recursiveTerm;
  }

  public void asUnion(final ExecutableSelect<?> anchorTerm, final ExecutableSelect<?> recursiveTerm) {
    this.anchorTerm = anchorTerm;
    this.unionAll = false;
    this.recursiveTerm = recursiveTerm;
  }

  @Override
  public boolean isRecursive() {
    return true;
  }

  @Override
  public void renderDefinitionTo(final QueryWriter w, final LiveSQLDialect dialect) {
    w.write(w.getSqlDialect().canonicalToNatural(w.getSqlDialect().naturalToCanonical(super.getAlias())));
    if (this.aliases != null && this.aliases.length > 0) {
      w.write(" (");
      w.write(Arrays.stream(this.aliases).map(a -> w.getSqlDialect().canonicalToNatural(a))
          .collect(Collectors.joining(", ")));
      w.write(")");
    }
    w.enterLevel();
    w.write(" as (\n");
    this.anchorTerm.renderTo(w);
    w.exitLevel();
    w.write("\n");
    w.write(this.unionAll ? "UNION ALL" : "UNION");
    w.write("\n");
    w.enterLevel();
    this.recursiveTerm.renderTo(w);
    w.exitLevel();
    w.write("\n");
    w.write(")");
  }

  public List<ResultSetColumn> getColumns() throws IllegalAccessException {
    return this.expandColumns(this.anchorTerm.listColumns());
  }

}
