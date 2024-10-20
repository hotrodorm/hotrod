package org.hotrod.runtime.livesql.queries.select;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.hotrod.runtime.cursors.Cursor;
import org.hotrod.runtime.livesql.dialects.JoinRenderer;
import org.hotrod.runtime.livesql.dialects.LiveSQLDialect;
import org.hotrod.runtime.livesql.dialects.LockingRenderer;
import org.hotrod.runtime.livesql.dialects.PaginationRenderer.PaginationType;
import org.hotrod.runtime.livesql.exceptions.InvalidLiveSQLStatementException;
import org.hotrod.runtime.livesql.exceptions.LiveSQLException;
import org.hotrod.runtime.livesql.expressions.ComparableExpression;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.Helper;
import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.metadata.Column;
import org.hotrod.runtime.livesql.metadata.MDHelper;
import org.hotrod.runtime.livesql.metadata.Name;
import org.hotrod.runtime.livesql.metadata.TableOrView;
import org.hotrod.runtime.livesql.ordering.OHelper;
import org.hotrod.runtime.livesql.ordering.OrderingTerm;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.QueryObject;
import org.hotrod.runtime.livesql.queries.QueryWriter;
import org.hotrod.runtime.livesql.queries.QueryWriter.LiveSQLPreparedQuery;
import org.hotrod.runtime.livesql.queries.ctes.CTE;
import org.hotrod.runtime.livesql.queries.ctes.RecursiveCTE;
import org.hotrod.runtime.livesql.queries.select.sets.MultiSet;
import org.hotrod.runtime.livesql.util.IdUtil;
import org.hotrodorm.hotrod.utils.SUtil;
import org.hotrodorm.hotrod.utils.Separator;
import org.springframework.util.ReflectionUtils;

public abstract class AbstractSelectObject<R> extends MultiSet<R> implements QueryObject {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(AbstractSelectObject.class.getName());

  private List<CTE> ctes = new ArrayList<>();
  private boolean distinct;
  private List<Expression> distinctOn = null;
  protected TableExpression baseTableExpression = null;
  protected List<Join> joins = null;
  private Predicate wherePredicate = null;
  private List<ComparableExpression> groupBy = null;
  private Predicate havingPredicate = null;

  private List<OrderingTerm> orderingTerms = null;
  private Integer offset = null;
  private Integer limit = null;

  public enum LockingMode {
    FOR_UPDATE, FOR_SHARE
  };

  public enum LockingConcurrency {
    WAIT, NO_WAIT, SKIP_LOCKED
  };

  private LockingMode lockingMode = null;
  private LockingConcurrency lockingConcurrency = null;
  private Number waitTime = null;

  protected AbstractSelectObject(final List<CTE> ctes, final boolean distinct) {
    super();
    this.setCTEs(ctes);
    this.distinct = distinct;
    this.distinctOn = null;
  }

  protected AbstractSelectObject(final List<CTE> ctes, final Expression[] distinctOn) {
    super();
    this.setCTEs(ctes);
    this.distinct = false;

    if (distinctOn == null || distinctOn.length == 0) {
      throw new LiveSQLException("The list of DISTINCT ON expressions cannot be empty.");
    }
    for (Expression e : distinctOn) {
      if (e == null) {
        throw new LiveSQLException("A DISTINCT ON expression cannot be null.");
      }
    }
    this.distinctOn = Arrays.asList(distinctOn);
  }

  public void setDistinctOn(final List<Expression> expressions) {
    this.distinctOn = expressions;
  }

  protected abstract void writeColumns(final QueryWriter w, final TableExpression baseTableExpression,
      final List<Join> joins);

  // Setters

  private void setCTEs(final List<CTE> ctes) {
    if (ctes != null) {
      for (CTE c : ctes) {
        this.ctes.add(c);
      }
    }
  }

  public void setBaseTableExpression(final TableExpression baseTableExpression) {
    this.baseTableExpression = baseTableExpression;
    this.joins = new ArrayList<Join>();
  }

  public void addJoin(final Join join) {
    this.joins.add(join);
  }

  public void setWhereCondition(final Predicate whereCondition) {
    this.wherePredicate = whereCondition;
  }

  public void setGroupBy(final List<ComparableExpression> groupBy) {
    this.groupBy = groupBy;
  }

  public void setHavingCondition(final Predicate havingCondition) {
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

  // Getters

  Predicate getWhereCondition() {
    return wherePredicate;
  }

  Predicate getHavingCondition() {
    return havingPredicate;
  }

  // Execute

  public void renderTo(final QueryWriter w) {
    this.renderTo(w, false);
  }

  @Override
  public void renderTo(final QueryWriter w, final boolean inline) {

    if (inline) {
      w.write("\n");
    }

    LiveSQLDialect liveSQLDialect = w.getSQLDialect();

    // CTEs

    if (!this.ctes.isEmpty()) {
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

    this.writeColumns(w, this.baseTableExpression, this.joins);

    // base table

    if (this.baseTableExpression == null) {

      String rwt = liveSQLDialect.getFromRenderer().renderFromWithoutATable();
      w.write(SUtil.isEmpty(rwt) ? "" : ("\n" + rwt));

    } else {

      w.write("\nFROM ");
      this.baseTableExpression.renderTo(w);

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
        j.getTableExpression().renderTo(w);

        try {
          PredicatedJoin pj = (PredicatedJoin) j;
          if (pj.getJoinPredicate() != null) { // on
            w.write(" ON ");
            Helper.renderTo(pj.getJoinPredicate(), w);
          } else { // using
            w.write(" USING (");
            Separator sep = new Separator();
            for (Column c : pj.getUsingColumns()) {
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
        Helper.renderTo(this.wherePredicate, w);
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
          Helper.renderTo(expr, w);
        }
      }

      // having

      if (this.havingPredicate != null) {
        w.write("\nHAVING ");
        Helper.renderTo(this.havingPredicate, w);
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

  @Override
  public List<R> execute(final LiveSQLContext context) {
    return this.execute(context, null);
  }

  public List<R> execute(final LiveSQLContext context, final String entityMapperStatement) {

    LiveSQLPreparedQuery q = this.prepareQuery(context);

    if (entityMapperStatement == null) {
      return executeLiveSQL(context, q);
    } else {
      return context.getSQLSession().selectList(entityMapperStatement, q.getConsolidatedParameters());
    }

  }

  @Override
  public Cursor<R> executeCursor(final LiveSQLContext context) {
    return this.executeCursor(context, null);
  }

  public Cursor<R> executeCursor(final LiveSQLContext context, final String entityMapperStatement) {

    LiveSQLPreparedQuery q = this.prepareQuery(context);
    if (entityMapperStatement == null) {
      return executeLiveSQLCursor(context, q);
    } else {
      return new MyBatisCursor<R>(
          context.getSQLSession().selectCursor(entityMapperStatement, q.getConsolidatedParameters()));
    }

  }

  @Override
  public R executeOne(final LiveSQLContext context) {
    return this.executeOne(context, null);
  }

  public R executeOne(final LiveSQLContext context, final String entityMapperStatement) {

    LiveSQLPreparedQuery q = this.prepareQuery(context);
    if (entityMapperStatement == null) {
      return executeLiveSQLOne(context, q);
    } else {
      return context.getSQLSession().selectOne(entityMapperStatement, q.getConsolidatedParameters());
    }

  }

  @Override
  public void validateTableReferences(final TableReferences tableReferences, final AliasGenerator ag) {
    if (this.baseTableExpression != null) {
      this.baseTableExpression.validateTableReferences(tableReferences, ag);
    }
    if (this.joins != null) {
      for (Join j : this.joins) {
        j.getTableExpression().validateTableReferences(tableReferences, ag);
      }
    }
    if (this.wherePredicate != null) {
      Helper.validateTableReferences(this.wherePredicate, tableReferences, ag);
//      this.wherePredicate.validateTableReferences(tableReferences, ag);
    }
    if (this.groupBy != null) {
      for (ComparableExpression e : this.groupBy) {
        Helper.validateTableReferences(e, tableReferences, ag);
//        e.validateTableReferences(tableReferences, ag);
      }
    }
    if (this.havingPredicate != null) {
      Helper.validateTableReferences(this.havingPredicate, tableReferences, ag);
//      this.havingPredicate.validateTableReferences(tableReferences, ag);
    }
    if (this.orderingTerms != null) {
      for (@SuppressWarnings("unused")
      OrderingTerm e : this.orderingTerms) {
        //
      }
    }

  }

  public static class TableReferences {

    private Set<TableOrView> tableReferences = new HashSet<>();

    private Set<RecursiveCTE> visitedRecursiveCTEs = new HashSet<>();
    private Set<RecursiveCTE> visitedRecursiveCTEs2 = new HashSet<>();

    private Set<String> aliases = new HashSet<String>();

    public void register(final String alias, final TableOrView tableOrView) {
      if (this.tableReferences.contains(tableOrView)) {
        throw new InvalidLiveSQLStatementException(
            "An instance of the " + tableOrView.getType() + " " + MDHelper.renderUnescapedName(tableOrView)
                + (alias == null ? " (with no alias)" : " (with alias '" + alias + "')")
                + " is used multiple times in the Live SQL statement (in the FROM clause, JOIN clause, or a subquery). "
                + "If you need to include the same " + tableOrView.getType()
                + " multiple times in the query you can get more instances of it using the DAO method new"
                + SUtil.upperFirst(tableOrView.getType()) + "(\"alias\").");
      }
      this.tableReferences.add(tableOrView);
    }

    public Set<TableOrView> getTableReferences() {
      return tableReferences;
    }

    public Set<String> getAliases() {
      return aliases;
    }

    public int size() {
      return this.tableReferences.size();
    }

    public boolean visited(final RecursiveCTE cte) {
      if (this.visitedRecursiveCTEs.contains(cte)) {
        return true;
      }
      this.visitedRecursiveCTEs.add(cte);
      return false;
    }

    public boolean visited2(final RecursiveCTE cte) {
      if (this.visitedRecursiveCTEs2.contains(cte)) {
        return true;
      }
      this.visitedRecursiveCTEs2.add(cte);
      return false;
    }

  }

  public static class AliasGenerator {

    private Set<String> used = new HashSet<String>();

    private char letter = 'a';
    private int seq = 0;

    public void register(final Name alias, final TableOrView tov) {
      if (alias == null) {
        return;
      }

      if (alias.getName().isEmpty()) {
        throw new InvalidLiveSQLStatementException("Empty alias found for " + tov.getType().toLowerCase() + " "
            + MDHelper.renderUnescapedName(tov) + ". Any specified alias for a table or view must be non-empty. "
            + "Use any combination of alphanumeric characters as an alias. "
            + "Usually aliases are very short, commonly a single letter.");
      }

      if (!this.used.add(alias.getName())) {
        throw new InvalidLiveSQLStatementException(
            "Same alias '" + alias + "' for tables/views cannot be used multiple times in a Live SQL statement. "
                + "If a query includes multiple tables or views "
                + "two of them cannot share the same alias, even when using subqueries.");
      }
    }

    public String next() {
      do {
        String alias = this.seq == 0 ? "" + this.letter : "" + this.letter + this.seq;
        if (this.used.add(alias)) {
          return alias;
        }
        if (this.letter >= 'z') {
          this.letter = 'a';
          this.seq++;
        } else {
          this.letter++;
        }
      } while (true);
    }

  }

  protected List<ResultSetColumn> getColumnsField(final Object cs, final String colName)
      throws IllegalArgumentException, IllegalAccessException {
    try {
      Field cf = ReflectionUtils.findField(cs.getClass(), colName);
      if (cf != null) {
        cf.setAccessible(true);
        Object object = cf.get(cs);
        @SuppressWarnings("unchecked")
        List<Column> columns = (List<Column>) object;
        return columns.stream().map(c -> (ResultSetColumn) c).collect(Collectors.toList());
      } else {
        return new ArrayList<>();
      }
    } catch (ClassCastException e) {
      e.printStackTrace();
      throw e;
    }
  }

  public final String toString() {
    return "s" + IdUtil.id(this) + (this.orderingTerms != null ? "o" : "");
  }

}
