package org.hotrod.runtime.livesql.queries.subqueries;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.runtime.livesql.exceptions.LiveSQLException;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.expressions.binary.ByteArrayExpression;
import org.hotrod.runtime.livesql.expressions.datetime.DateTimeExpression;
import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;
import org.hotrod.runtime.livesql.expressions.object.ObjectExpression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.expressions.strings.StringExpression;
import org.hotrod.runtime.livesql.queries.select.AbstractSelectObject.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelectObject.TableReferences;
import org.hotrod.runtime.livesql.queries.select.Select;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;
import org.hotrod.runtime.livesql.queries.select.SelectObject;
import org.hotrod.runtime.livesql.queries.select.TableExpression;
import org.hotrod.runtime.livesql.util.SubqueryUtil;
import org.hotrodorm.hotrod.utils.SUtil;

public class Subquery implements TableExpression {

  private String name;
  protected String[] columns;
  private SelectObject<?> select;

  protected Subquery(final String name, final String[] columns) {
    if (SUtil.isEmpty(name)) {
      throw new LiveSQLException("Subquery name cannot be empty", null);
    }

    this.name = name;
    this.columns = columns;
    this.select = null;
  }

  public Subquery(final String name, final String[] columns, final Select<?> es) {
    if (SUtil.isEmpty(name)) {
      throw new LiveSQLException("Subquery name cannot be empty", null);
    }
    this.name = name;

    this.columns = columns;

    if (es == null) {
      throw new LiveSQLException("Subquery select query cannot be null", null);
    }

    this.select = es.getSelect();
  }

  // Getters

  public String getName() {
    return name;
  }

  public SelectObject<?> getSelect() {
    return select;
  }

  // Subquery column extraction

  public NumberExpression num(final String name) {
    return new SubqueryNumberColumn(this, name);
  }

  public StringExpression str(final String name) {
    return new SubqueryStringColumn(this, name);
  }

  public DateTimeExpression dt(final String name) {
    return new SubqueryDateTimeColumn(this, name);
  }

  public Predicate bool(final String name) {
    return new SubqueryBooleanColumn(this, name);
  }

  public ByteArrayExpression bin(final String name) {
    return new SubqueryByteArrayColumn(this, name);
  }

  public ObjectExpression obj(final String name) {
    return new SubqueryObjectColumn(this, name);
  }

  public AllSubqueryColumns star() {
    return new AllSubqueryColumns(this);
  }

  // Table References

  @Override
  public void validateTableReferences(final TableReferences tableReferences, final AliasGenerator ag) {
    ag.register(this.name, null);
    this.select.validateTableReferences(tableReferences, ag);
  }

  // Rendering

  @Override
  public void renderTo(final QueryWriter w) {
    w.enterLevel();
    w.write("(\n");
    this.select.renderTo(w);
    w.exitLevel();
    w.write("\n");
    w.write(") ");
    w.write(w.getSqlDialect().canonicalToNatural(w.getSqlDialect().naturalToCanonical(this.name)));
    if (this.columns != null && this.columns.length > 0) {
      w.write(w.getSqlDialect().getTableExpressionRenderer().renderNamedColumns(this.columns));
    }
  }

  @Override
  public List<ResultSetColumn> getColumns() throws IllegalAccessException {
    return this.expandColumns(this.select.listColumns());
  }

  protected List<ResultSetColumn> expandColumns(final List<ResultSetColumn> cols) throws IllegalAccessException {
    List<ResultSetColumn> subqueryColumns = new ArrayList<>();
    for (ResultSetColumn c : cols) {
      Expression expr = castAsSubqueryColumn(c);
      subqueryColumns.add(expr);
    }
    return subqueryColumns;

  }

  private Expression castAsSubqueryColumn(final ResultSetColumn c)
      throws IllegalArgumentException, IllegalAccessException {
    return SubqueryUtil.castPersistenceColumnAsSubqueryColumn(this, c);
  }

}
