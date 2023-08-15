package org.hotrod.runtime.livesql.queries.subqueries;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.runtime.livesql.dialects.LiveSQLDialect;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.expressions.binary.ByteArrayExpression;
import org.hotrod.runtime.livesql.expressions.datetime.DateTimeExpression;
import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;
import org.hotrod.runtime.livesql.expressions.object.ObjectExpression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.expressions.strings.StringExpression;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.TableReferences;
import org.hotrod.runtime.livesql.queries.select.ExecutableSelect;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;
import org.hotrod.runtime.livesql.queries.select.TableExpression;
import org.hotrod.runtime.livesql.util.SubqueryUtil;

public class Subquery implements TableExpression {

  private String alias;
  private ExecutableSelect<?> select;

  public Subquery(final String alias, final ExecutableSelect<?> select) {
    this.alias = alias;
    this.select = select;
  }

  // Getters

  public String getAlias() {
    return alias;
  }

  public ExecutableSelect<?> getSelect() {
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

  // Validation

  @Override
  public void validateTableReferences(final TableReferences tableReferences, final AliasGenerator ag) {
    ag.register(this.alias, null);
  }

  @Override
  public void designateAliases(final AliasGenerator ag) {
    if (this.alias == null) {
      this.alias = ag.next();
    }
  }

  // Rendering

  @Override
  public void renderTo(QueryWriter w, LiveSQLDialect dialect) {
    w.enterLevel();
    w.write("(\n");
    this.select.renderTo(w);
    w.exitLevel();
    w.write("\n");
    w.write(") ");
    w.write(w.getSqlDialect().canonicalToNatural(w.getSqlDialect().naturalToCanonical(this.alias)));
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
