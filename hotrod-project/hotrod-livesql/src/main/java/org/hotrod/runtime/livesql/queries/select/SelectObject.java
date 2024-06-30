package org.hotrod.runtime.livesql.queries.select;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.Helper;
import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.metadata.Column;
import org.hotrod.runtime.livesql.queries.QueryWriter;
import org.hotrod.runtime.livesql.queries.ctes.CTE;
import org.hotrod.runtime.livesql.queries.subqueries.EmergingColumn;
import org.hotrod.runtime.livesql.queries.subqueries.SubqueryColumn;
import org.hotrodorm.hotrod.utils.Separator;

public class SelectObject<R> extends AbstractSelectObject<R> {

  private static final Logger log = Logger.getLogger(SelectObject.class.getName());

  private boolean doNotAliasColumns;
  private List<ResultSetColumn> resultSetColumns = new ArrayList<>();

  private List<EmergingColumn> expandedColumns;

  public SelectObject(final List<CTE> ctes, final boolean distinct, final boolean doNotAliasColumns) {
    super(ctes, distinct);
    this.doNotAliasColumns = doNotAliasColumns;
  }

  public SelectObject(final List<CTE> ctes, final boolean distinct, final boolean doNotAliasColumns,
      final List<ResultSetColumn> resultSetColumns) {
    super(ctes, distinct);
    this.doNotAliasColumns = doNotAliasColumns;
    this.resultSetColumns = resultSetColumns;
  }

  public SelectObject(final List<CTE> ctes, final Expression[] distinctOn, final boolean doNotAliasColumns,
      final List<ResultSetColumn> resultSetColumns) {
    super(ctes, distinctOn);
    this.doNotAliasColumns = doNotAliasColumns;
    this.resultSetColumns = resultSetColumns;
  }

  // Setters

  public void setResultSetColumns(final List<ResultSetColumn> resultSetColumns) {
    this.resultSetColumns = resultSetColumns;
  }

  // Rendering

  @Override
  protected List<EmergingColumn> assembleColumnsOf(final TableExpression te) {

    List<EmergingColumn> allColumns = new ArrayList<>();

    boolean listedColumns = this.resultSetColumns != null && !this.resultSetColumns.isEmpty();

    // 1. Compute columns of tables and subqueries in the FROM and JOIN clauses

    log.info("vvv assembling columns for: " + this.baseTableExpression.getName());
    List<EmergingColumn> fc = this.baseTableExpression.assembleColumns();
    logEmergingColumns(this.baseTableExpression, fc);
    allColumns.addAll(fc);
    log.info("^^^ assembled columns for: " + this.baseTableExpression.getName());

    for (Join j : this.joins) {
      log.info("vvv assembling columns for: " + j.getTableExpression().getName());
      List<EmergingColumn> jc = j.assembleColumns();
      logEmergingColumns(j.getTableExpression(), fc);
      allColumns.addAll(jc);
      log.info("^^^ assembled columns for: " + j.getTableExpression().getName());
    }

    if (listedColumns) {

      this.expandedColumns = new ArrayList<>();

      for (ResultSetColumn rsc : this.resultSetColumns) {
        log.info("---------- rsc=" + rsc);
        Expression expr = Helper.getExpression(rsc);
//        if (expr != null) {
//        expr.asEmergingColumnFrom(allColumns);
//        this.expandedColumns.add(expr);
//        } else {
//          this.expandedColumns.addAll(Helper.unwrap(rsc));
//        }
      }

    } else {
      this.expandedColumns = allColumns.stream().map(c -> c.asEmergingColumnOf(te)).collect(Collectors.toList());
    }

    // Log

    // 4. Return columns

    return this.expandedColumns;

  }

  private void x(Expression expr) {
    SubqueryColumn x = (SubqueryColumn) expr;
//    x
  }

  private void logEmergingColumns(final TableExpression te, List<EmergingColumn> ec) {
    log.info(" ");
    log.info("Emerging Columns of '" + te.getName().getName() + "':");
    for (EmergingColumn c : ec) {
      log.info(" * " + c);
    }
  }

  @Override
  protected void writeColumns(final QueryWriter w, final TableExpression baseTableExpression, final List<Join> joins) {
    Separator sep = new Separator();
    for (EmergingColumn c : this.expandedColumns) {

      w.write(sep.render());
      w.write("\n  ");
      c.renderTo(w);

      if (!this.doNotAliasColumns) {
        try {
          Column col = (Column) c;
          w.write(" as " + w.getSQLDialect().canonicalToNatural(col.getProperty()));
        } catch (ClassCastException e) {
          // Not a plain table/view column -- no need to alias it
        }
      }

    }
  }

  @Override
  public void flatten() {
    // Nothing to do. It's already a single level
  }

}
