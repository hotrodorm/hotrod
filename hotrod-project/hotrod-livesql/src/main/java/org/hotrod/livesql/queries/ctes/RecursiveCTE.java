package org.hotrod.livesql.queries.ctes;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.hotrod.livesql.dialects.LiveSQLDialect;
import org.hotrod.livesql.exceptions.LiveSQLException;
import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.Shield;
import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.livesql.queries.select.SShield;
import org.hotrod.livesql.queries.select.Select;
import org.hotrod.livesql.queries.select.TableReferences;
import org.hotrod.livesql.queries.select.UnarySelectObject.AliasGenerator;
import org.hotrod.livesql.queries.select.sets.CombinedSelectObject;
import org.hotrod.livesql.queries.select.sets.SelectObject;
import org.hotrod.livesql.queries.subqueries.Subquery;

public class RecursiveCTE extends CTE {

  private static final Logger log = Logger.getLogger(RecursiveCTE.class.getName());

  // Properties

  private Select<?> anchorTerm;
  private boolean unionAll;
  private Select<?> recursiveTerm;

  // Constructor

  public RecursiveCTE(final String name, final String[] columns) {
    super(name, columns);
    log.fine("init");
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
      SShield.getCombinedSelect(this.anchorTerm).validateTableReferences(tableReferences, ag);
      SShield.getCombinedSelect(this.recursiveTerm).validateTableReferences(tableReferences, ag);
    }
  }

  @Override
  protected void renderColumns(Set<SelectObject<?>> compiling) {

    CombinedSelectObject<?> anchorSelect = SShield.getCombinedSelect(this.anchorTerm);
    CombinedSelectObject<?> recursiveSelect = SShield.getCombinedSelect(this.recursiveTerm);

    anchorSelect.compileColumns(compiling);
    recursiveSelect.compileColumns(compiling);
    List<Expression> raw = anchorSelect.getCompiledColumns();
    // raw: has expanded all columns at this point.

    this.resolvedColumns = raw.stream().map(r -> Shield.asSubqueryExpression(r, this, Shield.getReferenceName(r)))
        .collect(Collectors.toList());

    this.columnsByName = this.resolvedColumns.stream()
        .collect(Collectors.toMap(c -> Shield.getReferenceName(c), c -> c));
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
      if (this.columnNames != null && this.columnNames.length > 0) { // explicit column names
        w.write(" (");
        w.write(Arrays.stream(this.columnNames).map(a -> w.getSQLDialect().canonicalToNatural(a))
            .collect(Collectors.joining(", ")));
        w.write(")");
      } else { // implicit column names from the anchor term
        w.write(" (");
        boolean first = true;
        List<Expression> cols = SShield.getCombinedSelect(this.anchorTerm).getCompiledColumns();
        for (Expression rc : cols) {
          if (first) {
            first = false;
          } else {
            w.write(", ");
          }
          Shield.renderTo(rc, w);
        }
        w.write(")");
      }
    }

    w.enterLevel();
    w.write(" as (\n");
    SShield.getCombinedSelect(this.anchorTerm).renderTo(w);
    w.exitLevel();
    w.write("\n");
    w.write(this.unionAll ? "UNION ALL" : "UNION");
    w.write("\n");
    w.enterLevel();
    SShield.getCombinedSelect(this.recursiveTerm).renderTo(w);
    w.exitLevel();
    w.write("\n");
    w.write(")");
  }

  // TODO: Clean up
//  @Override
//  protected List<ResultSetColumn> getColumns() throws IllegalAccessException {
//    return this.expandColumns(SHelper.getCombinedSelect(this.anchorTerm).listColumns());
//  }

}
