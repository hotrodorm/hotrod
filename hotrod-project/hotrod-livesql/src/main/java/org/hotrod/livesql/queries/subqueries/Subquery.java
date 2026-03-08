package org.hotrod.livesql.queries.subqueries;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.hotrod.livesql.exceptions.LiveSQLException;
import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.Shield;
import org.hotrod.livesql.expressions.binary.BinaryExpression;
import org.hotrod.livesql.expressions.character.CharExpression;
import org.hotrod.livesql.expressions.datetime.DateTimeExpression;
import org.hotrod.livesql.expressions.numeric.NumericExpression;
import org.hotrod.livesql.expressions.object.ObjectExpression;
import org.hotrod.livesql.metadata.Name;
import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.livesql.queries.select.SShield;
import org.hotrod.livesql.queries.select.Select;
import org.hotrod.livesql.queries.select.TableExpression;
import org.hotrod.livesql.queries.select.TableReferences;
import org.hotrod.livesql.queries.select.FlatSelectObject.AliasGenerator;
import org.hotrod.livesql.queries.select.sets.CombinedSelectObject;
import org.hotrod.livesql.queries.select.sets.MShield;
import org.hotrod.livesql.queries.select.sets.SelectObject;
import org.hotrod.livesql.util.ToString;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.utils.SUtil;

public class Subquery extends TableExpression {

  private static final Logger log = Logger.getLogger(Subquery.class.getName());

  private Name name;
  protected String[] columnNames;
  private CombinedSelectObject<?> select;

  protected List<Expression> resolvedColumns = null;
  protected Map<String, Expression> columnsByName = null;

  protected Subquery(final String naturalName, final String[] columnNames) {
    log.fine("init");
    if (SUtil.isEmpty(naturalName)) {
      throw new LiveSQLException("Subquery name cannot be empty", null);
    }
    this.name = Name.parse(naturalName);
    this.columnNames = columnNames;
    this.select = null;
  }

  public Subquery(final String naturalName, final String[] columnNames, final Select<?> es) {
    if (SUtil.isEmpty(naturalName)) {
      throw new LiveSQLException("Subquery name cannot be empty", null);
    }
    this.name = Name.parse(naturalName);

    this.columnNames = columnNames;

    if (es == null) {
      throw new LiveSQLException("Subquery select query cannot be null", null);
    }
    this.select = SShield.getCombinedSelect(es);
  }

  // Getters

  @Override
  protected Name getName() {
    return name;
  }

  protected CombinedSelectObject<?> getSelect() {
    return select;
  }

  // Subquery column reference

  public NumericExpression num(final String name) {
    return new NumericSubqueryExpression(this, name, null);
  }

  public CharExpression str(final String name) {
    return new CharSubqueryExpression(this, name, null);
  }

  public DateTimeExpression dt(final String name) {
    return new DateTimeSubqueryExpression(this, name, null);
  }

  public Predicate bool(final String name) {
    return new BooleanSubqueryExpression(this, name, null);
  }

  public BinaryExpression bin(final String name) {
    return new BinarySubqueryExpression(this, name, null);
  }

  public ObjectExpression obj(final String name) {
    return new ObjectSubqueryExpression(this, name, null);
  }

  public AllSubqueryColumns star() {
    return new AllSubqueryColumns(this);
  }

  // Table References

  @Override
  protected void validateTableReferences(final TableReferences tableReferences, final AliasGenerator ag) {
    ag.register(this.name, null);
    this.select.validateTableReferences(tableReferences, ag);
  }

  @Override
  protected void renderColumns(Set<SelectObject<?>> compiling) {
    this.select.compileColumns(compiling);
    List<Expression> raw = this.select.getCompiledColumns();
    // raw: has expanded all columns at this point.

    this.resolvedColumns = raw.stream().map(r -> Shield.asSubqueryExpression(r, this, Shield.getReferenceName(r)))
        .collect(Collectors.toList());

    this.columnsByName = this.resolvedColumns.stream()
        .collect(Collectors.toMap(c -> Shield.getReferenceName(c), c -> c));
  }

  List<Expression> getResolvedColumns() {
    return resolvedColumns;
  }

  Expression findColumnByName(final String property) {
    return this.columnsByName.get(property);
  }

  // Rendering

  @Override
  protected void renderTo(final QueryWriter w) {
    w.enterLevel();
    w.write("(\n");
    this.select.renderTo(w);
    w.exitLevel();
    w.write("\n");
    w.write(") ");
    this.name.renderTo(w);
    if (this.columnNames != null && this.columnNames.length > 0) {
      w.write(w.getSQLDialect().getTableExpressionRenderer().renderNamedColumns(this.columnNames));
    }
  }

  @Override
  protected final void log(ToString t) {
    t.printObject(this, "FROM " + this.name);
    t.printProperty("columns",
        this.columnNames == null ? null : Arrays.stream(this.columnNames).collect(Collectors.joining(", ")));
    if (this.resolvedColumns != null) {
      for (Expression expr : this.resolvedColumns) {
        t.printProperty("col", expr);
      }
    }
    t.indent();
    MShield.log(this.select, t);
    t.unindent();
  }

}
