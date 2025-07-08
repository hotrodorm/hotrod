package org.hotrod.livesql.queries.subqueries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.hotrod.livesql.exceptions.LiveSQLException;
import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.Shield;
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
import org.hotrod.livesql.queries.select.sets.MShield;
import org.hotrod.livesql.util.ToString;
import org.hotrod.utils.SUtil;

public class Subquery extends TableExpression {

  private static final Logger log = Logger.getLogger(Subquery.class.getName());

  private Name name;
  protected String[] columns;
  private CombinedSelectObject<?> select;

  private List<Expression> resolvedColumns = null;
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
    return new SubqueryNumberRefColumn(this, name, null);
  }

  public StringExpression str(final String name) {
    return new SubqueryStringRefColumn(this, name, null);
  }

  public DateTimeExpression dt(final String name) {
    return new SubqueryDateTimeRefColumn(this, name, null);
  }

  public BooleanExpression bool(final String name) {
    return new SubqueryBooleanRefColumn(this, name, null);
  }

  public ByteArrayExpression bin(final String name) {
    return new SubqueryByteArrayRefColumn(this, name, null);
  }

  public ObjectExpression obj(final String name) {
    return new SubqueryObjectRefColumn(this, name, null);
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
    log.info("ASSEMBLING COLUMNS FOR SUBQUERY: " + this.getName());

    List<Expression> raw = MShield.assembleColumnsOf(this.select, this);
    // raw: has expanded all columns at this point.

    this.resolvedColumns = new ArrayList<>();
    for (Expression r : raw) {
      log.info("- raw: " + this.getName() + "." + Shield.getReferenceName(r) + " r=" + r);
//      log.info("- raw: " + this.getName() + ".?" + " r=" + r);
    }
    this.resolvedColumns = raw.stream().map(r -> Shield.asSubqueryExpression(r, this, Shield.getReferenceName(r)))
        .collect(Collectors.toList());

    this.columnsByName = this.resolvedColumns.stream()
        .collect(Collectors.toMap(c -> Shield.getReferenceName(c), c -> c));
    for (Expression c : this.resolvedColumns) {
      log.info("$ RESOLVED - " + this.getName() + "." + Shield.getReferenceName(c) + ": " + c);
//      log.info("           --> " + Shield.getTypeHandler(c));
    }
    log.info("-- ASSEMBLING DONE FOR Subquery '" + this.name + "'");
    log.info("");
  }

  List<Expression> getResolvedColumns() {
    log.info("-- resolvedColumns(" + resolvedColumns.size() + ")");
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
    if (this.columns != null && this.columns.length > 0) {
      w.write(w.getSQLDialect().getTableExpressionRenderer().renderNamedColumns(this.columns));
    }
  }

  public void log(ToString t) {
    t.printObject(this, "FROM " + this.name);
    t.printProperty("columns",
        this.columns == null ? null : Arrays.stream(this.columns).collect(Collectors.joining(", ")));
    if (this.resolvedColumns != null) {
      for (Expression expr : this.resolvedColumns) {
        t.printProperty("col", expr);
      }
    }
    t.indent();
    this.select.log(t);
    t.unindent();
  }

}
