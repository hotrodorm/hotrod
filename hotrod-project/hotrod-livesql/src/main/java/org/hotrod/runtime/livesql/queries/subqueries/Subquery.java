package org.hotrod.runtime.livesql.queries.subqueries;

import java.util.List;
import java.util.logging.Logger;

import org.hotrod.runtime.livesql.exceptions.LiveSQLException;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.binary.ByteArrayExpression;
import org.hotrod.runtime.livesql.expressions.datetime.DateTimeExpression;
import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;
import org.hotrod.runtime.livesql.expressions.object.ObjectExpression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.expressions.strings.StringExpression;
import org.hotrod.runtime.livesql.metadata.Name;
import org.hotrod.runtime.livesql.queries.QueryWriter;
import org.hotrod.runtime.livesql.queries.select.AbstractSelectObject.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelectObject.TableReferences;
import org.hotrod.runtime.livesql.queries.select.SHelper;
import org.hotrod.runtime.livesql.queries.select.Select;
import org.hotrod.runtime.livesql.queries.select.TableExpression;
import org.hotrod.runtime.livesql.queries.select.sets.CombinedSelectObject;
import org.hotrod.runtime.livesql.queries.select.sets.MHelper;
import org.hotrodorm.hotrod.utils.SUtil;
import org.hotrodorm.hotrod.utils.TUtil;

public class Subquery extends TableExpression {

  private static final Logger log = Logger.getLogger(Subquery.class.getName());

  private Name name;
  protected String[] columns;
  private CombinedSelectObject<?> select;

  private List<Expression> expandedColumns = null;

  protected Subquery(final String naturalName, final String[] columns) {
    if (SUtil.isEmpty(naturalName)) {
      throw new LiveSQLException("Subquery name cannot be empty", null);
    }

    this.name = Name.parse(naturalName);
    this.columns = columns;
    this.select = null;
  }

  public Subquery(final String naturalName, final String[] columns, final Select<?> es) {
    if (SUtil.isEmpty(naturalName)) {
      throw new LiveSQLException("Subquery name cannot be empty", null);
    }
    this.name = Name.parse(naturalName);

    this.columns = columns;

    if (es == null) {
      throw new LiveSQLException("Subquery select query cannot be null", null);
    }

    this.select = SHelper.getCombinedSelect(es);
  }

  // Getters

  @Override
  protected Name getName() {
    return name;
  }

  public CombinedSelectObject<?> getSelect() {
    return select;
  }

  // Subquery column reference

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
  protected void validateTableReferences(final TableReferences tableReferences, final AliasGenerator ag) {
    ag.register(this.name, null);
    this.select.validateTableReferences(tableReferences, ag);
  }

  @Override
  protected List<Expression> assembleColumns() {
    log.info(">>> Subquery '" + this.name + "': assembleColumns() -- ");
    this.expandedColumns = MHelper.assembleColumns(this.select);
    log.info(">>> Subquery '" + this.name + "': done");
    return this.expandedColumns;
  }

  List<Expression> getExpandedColumns() {
    return expandedColumns;
  }

//  @Deprecated
//  @Override
//  protected void computeQueryColumns() {
//    log.info(
//        "--------------------------------------------------------------------------------------------------------------------");
//    log.info(">>> computeQueryColumns for subquery '" + this.name + "'...");
//    MHelper.computeQueryColumns(this.select);
//    log.info(">>> computeQueryColumns for subquery '" + this.name + "' complete");
//    log.info(
//        "--------------------------------------------------------------------------------------------------------------------");
//  }

  // Rendering

  @Override
  protected void renderTo(final QueryWriter w) {
    w.enterLevel();
    w.write("(\n");
    this.select.renderTo(w);
    w.exitLevel();
    w.write("\n");
    w.write(") ");

    if (this.name.isQuoted()) {
      w.write(w.getSQLDialect().quoteIdentifier(this.name.getName()));
    } else {
      w.write(w.getSQLDialect().canonicalToNatural(w.getSQLDialect().naturalToCanonical(this.name.getName())));
    }

    if (this.columns != null && this.columns.length > 0) {
      w.write(w.getSQLDialect().getTableExpressionRenderer().renderNamedColumns(this.columns));
    }
  }

//  @Override
//  protected List<Expression> getExpandedColumns() {
//    return this.expandColumns(this.select.listColumns());
//  }

//  protected List<Expression> expandColumns(final List<ResultSetColumn> cols) {
//    List<Expression> subqueryColumns = new ArrayList<>();
//    for (ResultSetColumn c : cols) {
//      Expression expr = castAsSubqueryColumn(c);
//      subqueryColumns.add(expr);
//    }
//    return subqueryColumns;
//
//  }

//  private Expression castAsSubqueryColumn(final ResultSetColumn c)
//      throws IllegalArgumentException, IllegalAccessException {
//    return SubqueryUtil.castPersistenceColumnAsSubqueryColumn(this, c);
//  }

}
