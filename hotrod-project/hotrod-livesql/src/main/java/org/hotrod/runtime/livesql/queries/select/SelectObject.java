package org.hotrod.runtime.livesql.queries.select;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.Helper;
import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.metadata.Column;
import org.hotrod.runtime.livesql.queries.QueryWriter;
import org.hotrod.runtime.livesql.queries.ctes.CTE;
import org.hotrodorm.hotrod.utils.Separator;

public class SelectObject<R> extends AbstractSelectObject<R> {

  private static final Logger log = Logger.getLogger(SelectObject.class.getName());

  private boolean doNotAliasColumns;
  private List<ResultSetColumn> resultSetColumns = new ArrayList<>();

  private List<Expression> queryColumns;

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

  private boolean columnsAssembled = false;

  public boolean areColumnsAssembled() {
    return columnsAssembled;
  }

  @Override
  public List<Expression> assembleColumnsOf(final TableExpression te) {

    boolean isListingColumns = this.resultSetColumns != null && !this.resultSetColumns.isEmpty();

    if (isListingColumns) {

      // sql.val(3).mult(7) -- Expression N/A
      // a.id -- Column te.id
      // x.num("amount") -- SubqueryXXXColumn te.amount

      // sql.val(3).mult(7).as("multi") -- Expression te.multi
      // a.id.as("bid") -- Column te.bid
      // x.num("amount").as("total") -- SubqueryXXXColumn te.total

      this.baseTableExpression.assembleColumns();
      this.joins.forEach(j -> j.getTableExpression().assembleColumns());

      this.queryColumns = new ArrayList<>();
      for (ResultSetColumn rsc : this.resultSetColumns) {
        Expression expr = Helper.getExpression(rsc);
        if (expr != null) {
          expr.captureTypeHandler();
          log.info("---------- expr@" + System.identityHashCode(expr) + ": " + expr);
          this.queryColumns.add(expr);
        } else {
          for (Expression exp : Helper.unwrap(rsc)) {
            exp.captureTypeHandler();
            log.info("---------- expr=" + exp);
            this.queryColumns.add(exp);
          }
        }
      }

      this.columnsAssembled = true;
      return this.queryColumns;

    } else { // columns not listed

      this.queryColumns = new ArrayList<>();

      // 1. Compute columns of tables and subqueries in the FROM and JOIN clauses

      log.info("vvv assembling columns for: " + this.baseTableExpression.getName());
      List<Expression> fc = this.baseTableExpression.assembleColumns();
      logEmergingColumns(this.baseTableExpression, fc);
      this.queryColumns.addAll(fc);
      log.info("^^^ assembled columns for: " + this.baseTableExpression.getName());

      for (Join j : this.joins) {
        log.info("vvv assembling columns for: " + j.getTableExpression().getName());
        List<Expression> jc = j.assembleColumns();
        logEmergingColumns(j.getTableExpression(), fc);
        this.queryColumns.addAll(jc);
        log.info("^^^ assembled columns for: " + j.getTableExpression().getName());
      }

      this.columnsAssembled = true;
      return this.queryColumns;

    }

  }

  @Override
  public Expression findColumnWithName(final String name) {
    for (Expression c : this.queryColumns) {
      if (name.equals(c.getReferenceName())) { // Only Entity columns, AliasedExpressions and SubqueryTTTColumns return
                                               // names.

        return c;
      }
    }
    return null;
  }

  private void logEmergingColumns(final TableExpression te, List<Expression> ec) {
    log.info(" ");
    log.info("- col '" + te.getName().getName() + "':");
    for (Expression c : ec) {
      log.info(" * " + c);
    }
  }

  @Override
  protected void writeColumns(final QueryWriter w, final TableExpression baseTableExpression, final List<Join> joins) {
    Separator sep = new Separator();
    for (Expression expr : this.queryColumns) {

      w.write(sep.render());
      w.write("\n  ");
      Helper.renderTo(expr, w);

//      Add alias?
//
//          Select Type           EntityCol  AliasedExpr  SubqueryCol  Other
//          --------------------  ---------  -----------  -----------  -----
//          Scalar SELECT         No         Yes          --           No
//          Criteria SELECT       No         Yes          --           No
//          Other/Main SELECT     Yes        Yes          Yes          No

      if (!this.doNotAliasColumns) { // other than scalar selects or criteria selects
        String property = expr.getProperty();
        if (property != null) {
          w.write(" as " + w.getSQLDialect().canonicalToNatural(property));
        }
      }
//      if (!this.doNotAliasColumns) { // other than scalar selects or criteria selects
//        try {
//          Column col = (Column) expr;
//          w.write(" as " + w.getSQLDialect().canonicalToNatural(col.getProperty()));
//        } catch (ClassCastException e) {
//          // Not a plain table/view column -- no need to alias it
//        }
//      }

    }
  }

  @Override
  public void flatten() {
    // Nothing to do. It's already a single level
  }

}
