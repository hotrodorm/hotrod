package org.hotrod.runtime.livesql.queries.ctes;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.hotrod.runtime.livesql.dialects.LiveSQLDialect;
import org.hotrod.runtime.livesql.exceptions.LiveSQLException;
import org.hotrod.runtime.livesql.queries.QueryWriter;
import org.hotrod.runtime.livesql.queries.select.AbstractSelectObject.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelectObject.TableReferences;
import org.hotrod.runtime.livesql.queries.select.SHelper;
import org.hotrod.runtime.livesql.queries.select.Select;
import org.hotrod.runtime.livesql.queries.select.sets.MHelper;
import org.hotrod.runtime.livesql.queries.subqueries.EmergingColumn;

public class RecursiveCTE extends CTE {

  // Properties

  private Select<?> anchorTerm;
  private boolean unionAll;
  private Select<?> recursiveTerm;

  // Constructor

  public RecursiveCTE(final String name, final String[] columns) {
    super(name, columns);
  }

  public void as(final Select<?> anchorTerm, final Select<?> recursiveTerm) {
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

  public void asUnion(final Select<?> anchorTerm, final Select<?> recursiveTerm) {
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
  protected void validateTableReferences(final TableReferences tableReferences, final AliasGenerator ag) {
    if (!tableReferences.visited(this)) {
      SHelper.getCombinedSelect(this.anchorTerm).validateTableReferences(tableReferences, ag);
      SHelper.getCombinedSelect(this.recursiveTerm).validateTableReferences(tableReferences, ag);
    }
  }

  // Rendering

  @Override
  public void renderDefinitionTo(final QueryWriter w, final LiveSQLDialect dialect) {

    if (super.getName().isQuoted()) {
      w.write(w.getSQLDialect().quoteIdentifier(super.getName().getName()));
    } else {
      w.write(w.getSQLDialect().canonicalToNatural(w.getSQLDialect().naturalToCanonical(super.getName().getName())));
    }

    if (w.getSQLDialect().mandatoryColumnNamesInRecursiveCTEs()) {
      if (this.columns != null && this.columns.length > 0) { // explicit column names
        w.write(" (");
        w.write(Arrays.stream(this.columns).map(a -> w.getSQLDialect().canonicalToNatural(a))
            .collect(Collectors.joining(", ")));
        w.write(")");
      } else { // implicit column names from the anchor term
        w.write(" (");
        boolean first = true;
        List<EmergingColumn> cols = MHelper.assembleColumns(SHelper.getCombinedSelect(this.anchorTerm));
        for (EmergingColumn rc : cols) {
          if (first) {
            first = false;
          } else {
            w.write(", ");
          }
          rc.renderTo(w);
        }
        w.write(")");
      }
    }

    w.enterLevel();
    w.write(" as (\n");
    SHelper.getCombinedSelect(this.anchorTerm).renderTo(w);
    w.exitLevel();
    w.write("\n");
    w.write(this.unionAll ? "UNION ALL" : "UNION");
    w.write("\n");
    w.enterLevel();
    SHelper.getCombinedSelect(this.recursiveTerm).renderTo(w);
    w.exitLevel();
    w.write("\n");
    w.write(")");
  }

//  @Override
//  protected List<ResultSetColumn> getColumns() throws IllegalAccessException {
//    return this.expandColumns(SHelper.getCombinedSelect(this.anchorTerm).listColumns());
//  }

}
