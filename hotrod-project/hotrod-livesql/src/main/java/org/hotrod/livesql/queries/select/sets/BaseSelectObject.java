package org.hotrod.livesql.queries.select.sets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.livesql.dialects.JoinRenderer;
import org.hotrod.livesql.dialects.LiveSQLDialect;
import org.hotrod.livesql.dialects.LockingRenderer;
import org.hotrod.livesql.dialects.PaginationRenderer.PaginationType;
import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.SQLExpression;
import org.hotrod.livesql.expressions.Shield;
import org.hotrod.livesql.expressions.bool.BooleanExpression;
import org.hotrod.livesql.metadata.EntityColumn;
import org.hotrod.livesql.metadata.MetaExpression;
import org.hotrod.livesql.ordering.OHelper;
import org.hotrod.livesql.ordering.OrderingTerm;
import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.livesql.queries.ctes.CTE;
import org.hotrod.livesql.queries.select.Join;
import org.hotrod.livesql.queries.select.PredicatedJoin;
import org.hotrod.livesql.queries.select.SShield;
import org.hotrod.livesql.queries.select.TableExpression;
import org.hotrod.livesql.queries.select.UnarySelectObject.LockingConcurrency;
import org.hotrod.livesql.queries.select.UnarySelectObject.LockingMode;
import org.hotrod.livesql.util.OUtil;
import org.hotrod.livesql.util.ToString;
import org.hotrod.utils.SUtil;
import org.hotrod.utils.Separator;

public abstract class BaseSelectObject<T> extends MultiSet<T> {

  private static final Logger log = Logger.getLogger(BaseSelectObject.class.getName());

  protected List<SQLExpression> resultSetColumns = new ArrayList<>();
  protected List<Expression> expandedQueryColumns = null;
  protected boolean columnsAssembled = false;

  protected List<CTE> ctes = new ArrayList<>();
  protected boolean distinct;
  protected List<Expression> distinctOn = null;

  protected TableExpression from = null;
  protected List<Join> joins = null;

  protected BooleanExpression wherePredicate = null;
  protected List<ComparableExpression> groupBy = null;
  protected BooleanExpression havingPredicate = null;
  protected List<OrderingTerm> orderingTerms = null;
  protected Integer offset = null;
  protected Integer limit = null;

  protected LockingMode lockingMode = null;
  protected LockingConcurrency lockingConcurrency = null;
  protected Number waitTime = null;

  public BaseSelectObject(List<CTE> ctes, boolean distinct) {
    this.ctes = ctes;
    this.distinct = distinct;
  }

  public final List<CTE> getCTEs() {
    return ctes;
  }

  public final boolean getDistinct() {
    return distinct;
  }

  public final List<SQLExpression> getResultSetColumns() {
    return resultSetColumns;
  }

  public boolean areColumnsAssembled() {
    return columnsAssembled;
  }

  protected void expandQueryColumns() {
    log.info("=== 2. EXPAND QUERY COLUMNS (AS/IF NEEDED) from " + SShield.getName(this.from) + " @" + OUtil.hc(this)
        + " ===");
    this.expandedQueryColumns = new ArrayList<>();
    for (SQLExpression rsc : this.resultSetColumns) {
      log.info("=== 2.1 rsc=" + rsc);

      try {
        // Single column
        Expression single = (Expression) rsc;
//        log.info("=== 2.2 single" + (single == null ? "" : " [" + single.getClass().getName() + "] ") + "=" + single);
        Expression emerging = Shield.getEmergingExpression(single); // emerging is always null for wrapping columns
//        log.info("=== 2.2 A emerging=" + emerging);
        if (emerging != null) {
          Shield.setTypeHandler(single, Shield.getTypeHandler(emerging));
        }
        this.expandedQueryColumns.add(single);

      } catch (ClassCastException cce) {
        // Wrapping column
//        log.info("=== 2.3");
        MetaExpression wrapping = (MetaExpression) rsc;
        for (Expression exp : Shield.expand(wrapping)) {
//          log.info("=== 2.4");
          Expression em = Shield.getEmergingExpression(exp);
          this.expandedQueryColumns.add(em);
        }
      }

//      log.info("=== 2.4 rsc=" + rsc);
//      Expression raised = Shield.getEmergingExpression(rsc); // raised is always null for wrapping columns
//
//      log.info("=== 2.4.1 raised=" + raised);
//      if (raised != null) {
//        this.expandedQueryColumns.add(raised);
//      } else {
//        for (Expression exp : Shield.expand(rsc)) {
//          raised = Shield.getEmergingExpression(exp);
//          this.expandedQueryColumns.add(raised);
//        }
//      }
    }
    log.info(">1 this@" + OUtil.hc(this) + ".expandedQueryColumns=" + this.expandedQueryColumns);

//    log.info("=== 2.10 EXPAND DONE from " + SShield.getName(this.from) + " ===");
  }

  public void setResultSetColumns(final List<SQLExpression> resultSetColumns) {
    this.resultSetColumns = resultSetColumns;
  }

  public void setBaseTableExpression(final TableExpression from) {
    this.from = from;
    this.joins = new ArrayList<Join>();
  }

  public void addJoin(final Join join) {
    this.joins.add(join);
  }

  public void setWhereCondition(final BooleanExpression whereCondition) {
    this.wherePredicate = whereCondition;
  }

  public void setGroupBy(final List<ComparableExpression> groupBy) {
    this.groupBy = groupBy;
  }

  public void setHavingCondition(final BooleanExpression havingCondition) {
    this.havingPredicate = havingCondition;
  }

  public void setColumnOrderings(final List<OrderingTerm> orderingTerms) {
    this.orderingTerms = orderingTerms;
  }

  public void setOffset(final int offset) {
    this.offset = offset;
  }

  public void setLimit(final int limit) {
    this.limit = limit;
  }

  public void setForUpdate() {
    this.lockingMode = LockingMode.FOR_UPDATE;
  }

  public void setForShare() {
    this.lockingMode = LockingMode.FOR_SHARE;
  }

  public void setLockingConcurrency(final Number waitTime, final boolean skipLocked) {
    if (skipLocked) {
      this.lockingConcurrency = LockingConcurrency.SKIP_LOCKED;
    } else if (waitTime == null) {
      this.lockingConcurrency = LockingConcurrency.NO_WAIT;
    } else {
      this.lockingConcurrency = LockingConcurrency.WAIT;
      this.waitTime = waitTime;
    }
  }

  // Render

  public void renderTo(final QueryWriter w) {
    this.renderTo(w, false);
  }

  @Override
  public void renderTo(final QueryWriter w, final boolean inline) {
//    log.info("=== 3. RENDER TO ===");

    if (inline) {
      w.write("\n");
    }

    LiveSQLDialect liveSQLDialect = w.getSQLDialect();

    // CTEs

    if (this.ctes != null && !this.ctes.isEmpty()) {
      boolean hasRecursiveCTEs = this.ctes.stream().map(c -> c.isRecursive()).reduce(false, (a, b) -> a | b);
      w.write(liveSQLDialect.getWithRenderer().render(hasRecursiveCTEs));
      w.write("\n");
      for (Iterator<CTE> it = this.ctes.iterator(); it.hasNext();) {
        CTE cte = it.next();
        cte.renderDefinitionTo(w, liveSQLDialect);
        if (it.hasNext()) {
          w.write(",");
        }
        w.write("\n");
      }
    }

    // retrieve pagination type

    boolean orderedSelect = this.orderingTerms != null && !this.orderingTerms.isEmpty();

    PaginationType paginationType = liveSQLDialect.getPaginationRenderer().getPaginationType(orderedSelect, this.offset,
        this.limit);

    // enclosing pagination - begin

    if ((this.offset != null || this.limit != null) && paginationType == PaginationType.ENCLOSE) {
      liveSQLDialect.getPaginationRenderer().renderBeginEnclosingPagination(this.offset, this.limit, w);
    }

    // select

    w.write("SELECT");

    // distinct

    if (this.distinct) {
      w.write(" DISTINCT");
    }

    // distinct on

    if (this.distinctOn != null) {
      liveSQLDialect.getDistinctOnRenderer().render(w, this.distinctOn);
    }

    // top offset & limit

    if ((this.offset != null || this.limit != null) && paginationType == PaginationType.TOP) {
      w.write("\n  ");
      liveSQLDialect.getPaginationRenderer().renderTopPagination(this.offset, this.limit, w);
    }

    // query columns

    this.writeColumns(w, this.from, this.joins);

    // base table

    if (this.from == null) {

      String rwt = liveSQLDialect.getFromRenderer().renderFromWithoutATable();
      w.write(SUtil.isEmpty(rwt) ? "" : ("\n" + rwt));

    } else {

      w.write("\nFROM ");
      SShield.renderTo(this.from, w);

      // Inline locking

      if (this.lockingMode != null) {
        LockingRenderer forUpdateRenderer = liveSQLDialect.getLockingRenderer();
        String fc = forUpdateRenderer.renderLockingAfterFromClause(this.lockingMode, this.lockingConcurrency,
            this.waitTime); // TODO
        if (fc != null) {
          w.write(" " + fc);
        }
      }

      // joins

      JoinRenderer joinRenderer = liveSQLDialect.getJoinRenderer();

      for (Join j : this.joins) {
        w.write("\n" + joinRenderer.renderJoinKeywords(j) + " ");
        SShield.renderTo(j, w);

        try {
          PredicatedJoin pj = (PredicatedJoin) j;
          if (pj.getJoinPredicate() != null) { // on
            w.write(" ON ");
            Shield.renderTo(pj.getJoinPredicate(), w);
          } else { // using
            w.write(" USING (");
            Separator sep = new Separator();
            for (EntityColumn c : pj.getUsingColumns()) {
              w.write(sep.render());
              w.write(w.getSQLDialect().canonicalToNatural(c.getReferenceName()));
            }
            w.write(")");
          }
        } catch (ClassCastException e) {
          // lateral joins may have extra dummy predicates
          w.write(joinRenderer.renderOptionalOnPredicate(j));
        }
      }

      // where

      if (this.wherePredicate != null) {
        w.write("\nWHERE ");
        Shield.renderTo(this.wherePredicate, w);
      }

      // group by

      if (this.groupBy != null && !this.groupBy.isEmpty()) {
        w.write("\nGROUP BY ");
        boolean first = true;
        for (ComparableExpression expr : this.groupBy) {
          if (first) {
            first = false;
          } else {
            w.write(", ");
          }
          Shield.renderTo(expr, w);
        }
      }

      // having

      if (this.havingPredicate != null) {
        w.write("\nHAVING ");
        Shield.renderTo(this.havingPredicate, w);
      }

    }

    // order by (combined selects can have ORDER BY without a FROM clause

    if (orderedSelect) {
      w.write("\nORDER BY ");
      boolean first = true;
      for (OrderingTerm term : this.orderingTerms) {
        if (first) {
          first = false;
        } else {
          w.write(", ");
        }
        OHelper.renderTo(term, w);
      }
    }

    // bottom offset & limit

    if ((this.offset != null || this.limit != null) && paginationType == PaginationType.BOTTOM) {
      liveSQLDialect.getPaginationRenderer().renderBottomPagination(this.offset, this.limit, w);
    }

    // Locking clause

    if (this.lockingMode != null) {
      LockingRenderer lockingRenderer = liveSQLDialect.getLockingRenderer();
      String lc = lockingRenderer.renderLockingAfterLimitClause(this.lockingMode, this.lockingConcurrency,
          this.waitTime); // TODO
      if (lc != null) {
        w.write("\n" + lc);
      }
    }

    // enclosing pagination - end

    if ((this.offset != null || this.limit != null) && paginationType == PaginationType.ENCLOSE) {
      liveSQLDialect.getPaginationRenderer().renderEndEnclosingPagination(this.offset, this.limit, w);
    }

  }

  protected abstract void writeColumns(final QueryWriter w, final TableExpression baseTableExpression,
      final List<Join> joins);

//  protected void writeColumns(final QueryWriter w, final TableExpression baseTableExpression, final List<Join> joins) {
//    Separator sep = new Separator();
//    for (Expression expr : this.queryColumns) {
//
//      w.write(sep.render());
//      w.write("\n  ");
//      Helper.renderTo(expr, w);
//
////      Add alias?
////
////          Select Type           EntityCol  AliasedExpr  SubqueryCol  Other
////          --------------------  ---------  -----------  -----------  -----
////          Scalar SELECT         No         Yes          --           No
////          Criteria SELECT       No         Yes          --           No
////          Other/Main SELECT     Yes        Yes          Yes          No
//
//      if (!this.doNotAliasColumns) { // other than scalar selects or criteria selects
//        String property = Helper.getProperty(expr);
//        if (property != null) {
//          w.write(" as " + w.getSQLDialect().canonicalToNatural(property));
//        }
//      }
//
//    }
//  }

  // Getters

  BooleanExpression getWhereCondition() {
    return wherePredicate;
  }

  BooleanExpression getHavingCondition() {
    return havingPredicate;
  }

  // log

  @Override
  protected void log(ToString t) {
    t.printObject(this, this.getClass().getName());
    if (this.resultSetColumns != null) {
      for (SQLExpression r : this.resultSetColumns) {
        t.indent();
        t.prompt("rsc - " + r.toString() + " // ");
        Shield.log(r, t);
        t.unindent();
      }
    }
    if (this.expandedQueryColumns != null) {
      for (Expression expr : this.expandedQueryColumns) {
        t.indent();
        t.prompt("expr");
        Shield.log(expr, t);
        t.unindent();
      }
    }
    t.indent();
    if (this.from != null) {
      SShield.log(this.from, t);
    }
    if (this.joins != null) {
      for (Join j : this.joins) {
        TableExpression te = SShield.getTableExpression(j);
        t.indent();
        SShield.log(te, t);
        t.unindent();
      }
    }
    t.unindent();

  }

}
