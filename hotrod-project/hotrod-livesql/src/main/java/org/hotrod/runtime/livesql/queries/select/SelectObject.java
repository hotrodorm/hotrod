package org.hotrod.runtime.livesql.queries.select;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.Helper;
import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.expressions.TypeHandler;
import org.hotrod.runtime.livesql.metadata.Column;
import org.hotrod.runtime.livesql.queries.QueryWriter;
import org.hotrod.runtime.livesql.queries.ctes.CTE;
import org.hotrodorm.hotrod.utils.Separator;

public class SelectObject<R> extends AbstractSelectObject<R> {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(SelectObject.class.getName());

  private boolean doNotAliasColumns;
  private List<ResultSetColumn> resultSetColumns = new ArrayList<>();

  private List<Expression> expandedColumns;

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
  protected List<Expression> assembleColumns() {

    this.expandedColumns = new ArrayList<>();

    boolean listedColumns = this.resultSetColumns != null && !this.resultSetColumns.isEmpty();

    // 1. Compute columns of subqueries in the FROM and JOIN clauses

    log.info("-- assembling columns for: " + this.baseTableExpression.getName());
    List<Expression> fc = this.baseTableExpression.assembleColumns();
    if (!listedColumns) {
      this.expandedColumns.addAll(fc);
    }
    log.info("-- *** assembled columns for: " + this.baseTableExpression.getName());

    for (Join j : this.joins) {
      log.info("-- assembling columns for: " + j.getTableExpression().getName());
      List<Expression> jc = j.assembleColumns();
      if (!listedColumns) {
        this.expandedColumns.addAll(jc);
      }
      log.info("-- *** assembled columns for: " + j.getTableExpression().getName());
    }

    if (listedColumns) {

      for (ResultSetColumn rsc : this.resultSetColumns) {
        Expression expr = Helper.getExpression(rsc);
        if (expr != null) {
          this.expandedColumns.add(expr);
        } else {
          this.expandedColumns.addAll(Helper.unwrap(rsc));
        }
      }

    }

    // Log

    log.info(" ");
    log.info(" Expanded Columns");
    for (Expression expr : this.expandedColumns) {
      String alias = Helper.getAlias(expr);
      TypeHandler th = Helper.getTypeHandler(expr);
      log.info(" * " + alias + ": " + th);
    }

    // 4. Return columns

    return this.expandedColumns;

  }

  @Override
  protected void writeColumns(final QueryWriter w, final TableExpression baseTableExpression, final List<Join> joins) {
    Separator sep = new Separator();
    for (Expression c : this.expandedColumns) {

      w.write(sep.render());
      w.write("\n  ");
      Helper.renderTo(c, w);

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
