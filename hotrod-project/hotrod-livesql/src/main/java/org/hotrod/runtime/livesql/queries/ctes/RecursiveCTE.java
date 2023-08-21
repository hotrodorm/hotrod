package org.hotrod.runtime.livesql.queries.ctes;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.hotrod.runtime.livesql.dialects.LiveSQLDialect;
import org.hotrod.runtime.livesql.exceptions.LiveSQLException;
import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.TableReferences;
import org.hotrod.runtime.livesql.queries.select.ExecutableSelect;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class RecursiveCTE extends CTE {

  // Properties

  private ExecutableSelect<?> anchorTerm;
  private boolean unionAll;
  private ExecutableSelect<?> recursiveTerm;

  // Constructor

  public RecursiveCTE(final String name, final String[] columns) {
    super(name, columns);
  }

  public void as(final ExecutableSelect<?> anchorTerm, final ExecutableSelect<?> recursiveTerm) {
    if (anchorTerm == null) {
      throw new LiveSQLException("The anchor term of a recursive CTE cannot be null", null);
    }
    if (recursiveTerm == null) {
      throw new LiveSQLException("The recursive term of a recursive CTE cannot be null", null);
    }

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

  // Table References

  @Override
  public void validateTableReferences(final TableReferences tableReferences, final AliasGenerator ag) {
    if (!tableReferences.visited(this)) {
      this.anchorTerm.validateTableReferences(tableReferences, ag);
      this.recursiveTerm.validateTableReferences(tableReferences, ag);
    }
  }

  // Rendering

  @Override
  public void renderDefinitionTo(final QueryWriter w, final LiveSQLDialect dialect) {
    w.write(w.getSqlDialect().canonicalToNatural(w.getSqlDialect().naturalToCanonical(super.getName())));
    if (this.columns != null && this.columns.length > 0) {
      w.write(" (");
      w.write(Arrays.stream(this.columns).map(a -> w.getSqlDialect().canonicalToNatural(a))
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
