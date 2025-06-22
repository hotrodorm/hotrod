package org.hotrod.livesql.queries.subqueries;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.hotrod.livesql.exceptions.LiveSQLException;
import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.Helper;
import org.hotrod.livesql.expressions.binary.ByteArrayExpression;
import org.hotrod.livesql.expressions.datetime.DateTimeExpression;
import org.hotrod.livesql.expressions.numbers.NumberExpression;
import org.hotrod.livesql.expressions.object.ObjectExpression;
import org.hotrod.livesql.expressions.predicates.BooleanExpression;
import org.hotrod.livesql.expressions.strings.StringExpression;
import org.hotrod.livesql.metadata.Name;
import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.livesql.queries.select.SShield;
import org.hotrod.livesql.queries.select.Select;
import org.hotrod.livesql.queries.select.TableExpression;
import org.hotrod.livesql.queries.select.TableReferences;
import org.hotrod.livesql.queries.select.UnarySelectObject.AliasGenerator;
import org.hotrod.livesql.queries.select.sets.CombinedSelectObject;
import org.hotrod.livesql.queries.select.sets.MHelper;
import org.hotrod.utils.SUtil;

public class Subquery extends TableExpression {

  private static final Logger log = Logger.getLogger(Subquery.class.getName());

  private Name name;
  protected String[] columns;
  private CombinedSelectObject<?> select;

  private List<Expression> expandedColumns = null;
  private Map<String, Expression> columnsByName = null;

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

  public NumberExpression num(final String name) {
    return new SubqueryNumberColumn(this, name);
  }

  public StringExpression str(final String name) {
    return new SubqueryStringColumn(this, name);
  }

  public DateTimeExpression dt(final String name) {
    return new SubqueryDateTimeColumn(this, name);
  }

  public BooleanExpression bool(final String name) {
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
  protected void assembleColumns() {
    List<Expression> raw = MHelper.assembleColumnsOf(this.select, this);
    this.expandedColumns = new ArrayList<>();
    for (Expression r : raw) {
      log.info("!!!! getProperty=" + Helper.getProperty(r) + " getReferenceName=" + Helper.getReferenceName(r));
//   String alias=   Helper.getProperty(r);
//      this.expandedColumns.add(Helper.asSubqueryExpression(r, this, alias));      
    }
    this.expandedColumns = raw.stream().map(r -> Helper.asSubqueryExpression(r, this, Helper.getReferenceName(r)))
        .collect(Collectors.toList());

    this.columnsByName = this.expandedColumns.stream()
        .collect(Collectors.toMap(c -> Helper.getReferenceName(c), c -> c));
    this.columnsByName.keySet().stream().forEach(c -> System.out.println("*** column=" + c));
    logEmergingColumns(this.expandedColumns);
  }

  @SuppressWarnings("unused")
  private void logEmergingColumns(List<Expression> ec) {
    log.info("$$$$$$ Columns (" + ec.size() + "):");
    for (Expression c : ec) {
      log.info("$$$$$$ @" + System.identityHashCode(c) + " * " + c);
    }
  }

  List<Expression> getExpandedColumns() {
    log.info("-- expandedColumns(" + expandedColumns.size() + ")");
    return expandedColumns;
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
    if (this.columns != null && this.columns.length > 0) {
      w.write(w.getSQLDialect().getTableExpressionRenderer().renderNamedColumns(this.columns));
    }
  }

}
