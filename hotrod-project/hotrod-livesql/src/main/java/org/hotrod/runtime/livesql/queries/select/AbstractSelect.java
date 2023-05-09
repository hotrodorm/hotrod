package org.hotrod.runtime.livesql.queries.select;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.apache.ibatis.session.SqlSession;
import org.hotrod.runtime.cursors.Cursor;
import org.hotrod.runtime.livesql.LiveSQLMapper;
import org.hotrod.runtime.livesql.dialects.LiveSQLDialect;
import org.hotrod.runtime.livesql.dialects.PaginationRenderer.PaginationType;
import org.hotrod.runtime.livesql.dialects.SetOperationRenderer.SetOperation;
import org.hotrod.runtime.livesql.exceptions.InvalidLiveSQLStatementException;
import org.hotrod.runtime.livesql.exceptions.LiveSQLException;
import org.hotrod.runtime.livesql.exceptions.UnsupportedLiveSQLFeatureException;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.metadata.AllColumns.ColumnSubset;
import org.hotrod.runtime.livesql.metadata.Column;
import org.hotrod.runtime.livesql.metadata.DatabaseObject;
import org.hotrod.runtime.livesql.metadata.TableOrView;
import org.hotrod.runtime.livesql.ordering.OrderingTerm;
import org.hotrod.runtime.livesql.queries.select.QueryWriter.LiveSQLStructure;
import org.hotrodorm.hotrod.utils.CUtil;
import org.hotrodorm.hotrod.utils.HexaUtils;
import org.hotrodorm.hotrod.utils.SUtil;
import org.hotrodorm.hotrod.utils.Separator;
import org.springframework.util.ReflectionUtils;

public abstract class AbstractSelect<R> extends Query {

  private boolean distinct;
  private TableOrView baseTable = null;
  private List<Join> joins = null;
  private Predicate wherePredicate = null;
  private List<Expression> groupBy = null;
  private Predicate havingPredicate = null;

  private SetOperation setOperation = null;
  private CombinableSelect<R> combinedSelect = null;
  private AbstractSelect<R> parent = null;

  private List<OrderingTerm> orderingTerms = null;
  private Integer offset = null;
  private Integer limit = null;

  private SqlSession sqlSession;
  private String mapperStatement;
  private LiveSQLMapper liveSQLMapper;

  AbstractSelect(final LiveSQLDialect sqlDialect, final boolean distinct, final SqlSession sqlSession,
      final String mapperStatement, final LiveSQLMapper liveSQLMapper) {
    super(sqlDialect);
    this.distinct = distinct;
    this.sqlSession = sqlSession;
    this.mapperStatement = mapperStatement;
    this.liveSQLMapper = liveSQLMapper;
  }

  protected abstract void writeColumns(final QueryWriter w, final TableOrView baseTable, final List<Join> joins);

  protected void writeExpandedColumns(final QueryWriter w, final TableOrView baseTable, final List<Join> joins,
      final List<ResultSetColumn> resultSetColumns, final boolean doNotAliasColumns) {

    List<ResultSetColumn> expandedColumns = new ArrayList<>(resultSetColumns);

    // 1. Expand unlisted columns

    try {
      if (expandedColumns == null || expandedColumns.isEmpty()) {
        expandedColumns = new ArrayList<>();
        List<Column> columns = getColumnsField(baseTable, TableOrView.class, "columns");
        for (Column e : columns) {
          expandedColumns.add(e);
        }
        for (Join j : joins) {
          columns = getColumnsField(j.getTable(), TableOrView.class, "columns");
          for (Column e : columns) {
            expandedColumns.add(e);
          }
        }
      }
    } catch (IllegalAccessException e) {
      throw new LiveSQLException("Could not expand LiveSQL columns (all)", e);
    } catch (RuntimeException e) {
      throw new LiveSQLException("Could not expand LiveSQL columns (all)", e);
    }

    // 2. Expand columns subsets (star(), filtered star(), etc.)

    ListIterator<ResultSetColumn> it = expandedColumns.listIterator();
    while (it.hasNext()) {
      try {
        ResultSetColumn rsc = it.next();
        ColumnSubset cs = (ColumnSubset) rsc;
        it.remove();
        List<Column> columns = getColumnsField(cs, ColumnSubset.class, "columns");
        for (Column fc : columns) {
          it.add(fc);
        }

      } catch (SecurityException e) {
        throw new LiveSQLException("Could not expand subset of LiveSQL columns", e);
      } catch (IllegalArgumentException e) {
        throw new LiveSQLException("Could not expand subset of LiveSQL columns", e);
      } catch (IllegalAccessException e) {
        throw new LiveSQLException("Could not expand subset of LiveSQL columns", e);
      } catch (ClassCastException e) {
        // Ignore. It's not a subset
      }

    }

    // 3. Render columns

    Separator sep = new Separator();
    for (ResultSetColumn c : expandedColumns) {

      w.write(sep.render());
      w.write("\n  ");
      c.renderTo(w);

      if (!doNotAliasColumns) {
        try {
          Column col = (Column) c;
          w.write(" as " + w.getSqlDialect().canonicalToNatural(col.getProperty()));
        } catch (ClassCastException e) {
          // Not a plain table/view column -- no need to alias it
        }
      }
    }

  }

  // Setters

  void setBaseTable(final TableOrView baseTable) {
    this.baseTable = baseTable;
    this.joins = new ArrayList<Join>();
  }

  void addJoin(final Join join) {
    this.joins.add(join);
  }

  void setWhereCondition(final Predicate whereCondition) {
    this.wherePredicate = whereCondition;
  }

  void setGroupBy(final List<Expression> groupBy) {
    this.groupBy = groupBy;
  }

  void setHavingCondition(final Predicate havingCondition) {
    this.havingPredicate = havingCondition;
  }

  void setCombinedSelect(final SetOperation setOperation, final CombinableSelect<R> combinedSelect) {
    this.setOperation = setOperation;
    this.combinedSelect = combinedSelect;
  }

  void setParent(final AbstractSelect<R> parent) {
    this.parent = parent;
  }

  void setColumnOrderings(List<OrderingTerm> orderingTerms) {
    this.orderingTerms = orderingTerms;
  }

  void setOffset(final int offset) {
    this.offset = offset;
  }

  void setLimit(final int limit) {
    this.limit = limit;
  }

  // Getters

  Predicate getWhereCondition() {
    return wherePredicate;
  }

  Predicate getHavingCondition() {
    return havingPredicate;
  }

  // Execute

  private LiveSQLStructure prepareQuery() {
    validateQuery();
    QueryWriter w = new QueryWriter(this.sqlDialect);
    renderTo(w);
    return w.getPreparedQuery();
  }

  public void renderTo(final QueryWriter w) {

    // retrieve pagination type

    PaginationType paginationType = this.sqlDialect.getPaginationRenderer().getPaginationType(this.offset, this.limit);

    // enclosing pagination - begin

    if ((this.offset != null || this.limit != null) && paginationType == PaginationType.ENCLOSE) {
      this.sqlDialect.getPaginationRenderer().renderBeginEnclosingPagination(this.offset, this.limit, w);
    }

    // select

    w.write("SELECT");

    // distinct

    if (this.distinct) {
      w.write(" distinct");
    }

    // top offset & limit

    if ((this.offset != null || this.limit != null) && paginationType == PaginationType.TOP) {
      w.write("\n  ");
      this.sqlDialect.getPaginationRenderer().renderTopPagination(this.offset, this.limit, w);
    }

    // query columns

    this.writeColumns(w, this.baseTable, this.joins);

    // base table

    if (this.baseTable == null) {

      w.write("\n" + this.sqlDialect.getFromRenderer().renderFromWithoutATable());

    } else {

      String alias = this.baseTable.getAlias() == null ? null
          : this.sqlDialect.canonicalToNatural(this.sqlDialect.naturalToCanonical(this.baseTable.getAlias()));

      w.write("\nFROM " + this.sqlDialect.canonicalToNatural(this.baseTable) + (alias != null ? (" " + alias) : ""));

      // joins

      for (Join j : this.joins) {
        String joinKeywords;
        joinKeywords = this.sqlDialect.getJoinRenderer().renderJoinKeywords(j);

        alias = j.getTable().getAlias() == null ? null
            : this.sqlDialect.canonicalToNatural(this.sqlDialect.naturalToCanonical(j.getTable().getAlias()));

        w.write("\n" + joinKeywords + " " + this.sqlDialect.canonicalToNatural(j.getTable())
            + (alias != null ? (" " + alias) : ""));
        try {
          PredicatedJoin pj = (PredicatedJoin) j;
          if (pj.getJoinPredicate() != null) { // on
            w.write(" ON ");
            pj.getJoinPredicate().renderTo(w);
          } else { // using
            w.write(" USING (");
            Separator sep = new Separator();
            for (Column c : pj.getUsingColumns()) {
              w.write(sep.render());
              c.renderUnqualifiedNameTo(w);
            }
            w.write(")");
          }
        } catch (ClassCastException e) {
          // no predicate on join - continue
        }
      }

      // where

      if (this.wherePredicate != null) {
        w.write("\nWHERE ");
        this.wherePredicate.renderTo(w);
      }

      // group by

      if (this.groupBy != null && !this.groupBy.isEmpty()) {
        w.write("\nGROUP BY ");
        boolean first = true;
        for (Expression expr : this.groupBy) {
          if (first) {
            first = false;
          } else {
            w.write(", ");
          }
          expr.renderTo(w);
        }
      }

      // having

      if (this.havingPredicate != null) {
        w.write("\nHAVING ");
        this.havingPredicate.renderTo(w);
      }

      // combined select

      if (this.combinedSelect != null) {
        w.write("\n");
        this.sqlDialect.getSetOperationRenderer().render(this.setOperation, w);
        w.write("\n");
        this.combinedSelect.renderTo(w);
      }

      // order by

      if (this.orderingTerms != null && !this.orderingTerms.isEmpty()) {

        if (this.combinedSelect != null || this.parent != null) {

          // ORDER BY still unsupported for combined selects. Need to study this
          // feature more.

          // PostgreSQL only supports named columns from the initial SELECT as
          // ordering columns; not expressions or functions. The error reads:
          // Error: ERROR: invalid UNION/INTERSECT/EXCEPT ORDER BY clause
          // Detail: Only result column names can be used, not expressions or
          // functions.
          // Hint: Add the expression/function to every SELECT, or move the
          // UNION
          // into a FROM clause.

          // A valid ORDER BY would read: ORDER BY "currentBalance"
          // Note that that's the alias of a column/expression, not the column
          // itself.

          throw new UnsupportedLiveSQLFeatureException(
              "HotRod does not yet support ORDER BY for combined queries (UNION, UNION ALL, INTERSECT, INTERSECT ALL, EXCEPT, or EXCEPT ALL).");
        }

        w.write("\nORDER BY ");
        boolean first = true;
        for (OrderingTerm term : this.orderingTerms) {
          if (first) {
            first = false;
          } else {
            w.write(", ");
          }
          term.renderTo(w);
        }
      }
    }

    // bottom offset & limit

    if ((this.offset != null || this.limit != null) && paginationType == PaginationType.BOTTOM) {
      this.sqlDialect.getPaginationRenderer().renderBottomPagination(this.offset, this.limit, w);
    }

    // enclosing pagination - end

    if ((this.offset != null || this.limit != null) && paginationType == PaginationType.ENCLOSE) {
      this.sqlDialect.getPaginationRenderer().renderEndEnclosingPagination(this.offset, this.limit, w);
    }

  }

  public List<R> execute() {

    LiveSQLStructure q = this.prepareQuery();

    if (this.mapperStatement == null) {
      return executeLiveSQL(q);
    } else {
      return this.sqlSession.selectList(this.mapperStatement, q.getConsolidatedParameters());
    }

  }

  public Cursor<R> executeCursor() {
    LiveSQLStructure q = this.prepareQuery();
    if (this.mapperStatement == null) {
      return executeLiveSQLCursor(q);
    } else {
      return new MyBatisCursor<R>(this.sqlSession.selectCursor(this.mapperStatement, q.getConsolidatedParameters()));
    }
  }

  @SuppressWarnings("unchecked")
  private List<R> executeLiveSQL(final LiveSQLStructure q) {
    LinkedHashMap<String, Object> parameters = q.getParameters();
    parameters.put("sql", q.getSQL());
    return (List<R>) this.liveSQLMapper.select(parameters);
  }

  @SuppressWarnings("unchecked")
  private Cursor<R> executeLiveSQLCursor(final LiveSQLStructure q) {
    LinkedHashMap<String, Object> parameters = q.getParameters();
    parameters.put("sql", q.getSQL());
    return (Cursor<R>) this.liveSQLMapper.selectCursor(parameters);
  }

  public String getPreview() {

    LiveSQLStructure q = this.prepareQuery();

    StringBuilder sb = new StringBuilder();
    sb.append("--- SQL ----------\n");
    sb.append(q.getSQL());
    sb.append("\n--- Parameters ---\n");
    for (String name : q.getParameters().keySet()) {
      Object value = q.getParameters().get(name);
      Integer length = null;
      String preview = null;
      if (value instanceof String) {
        String v = (String) value;
        length = v.length();
        if (length <= 250) {
          preview = v;
        } else {
          preview = v.substring(0, 250) + "...";
        }
      } else if (value instanceof byte[]) {
        byte[] v = (byte[]) value;
        length = v.length;
        if (v.length < 100) {
          preview = HexaUtils.toHexa(v);
        } else {
          preview = HexaUtils.toHexa(v, 0, 100) + "...";
        }
      } else {
        preview = "" + value;
      }

      sb.append(" * " + name
          + (value == null ? ""
              : " (" + CUtil.renderObjectClass(value) + (length == null ? "" : ", length=" + length) + ")")
          + ": " + preview + "\n");

    }
    sb.append("------------------");
    return sb.toString();

  }

  // Validation

  private void validateQuery() {

    // 1. Validate a table/view is not used more than once

    TableReferences tableReferences = new TableReferences();
    AliasGenerator ag = new AliasGenerator();
    this.validateTableReferences(tableReferences, ag);

    // 2. Designate aliases to tables/views that do not declare them

    if (tableReferences.getTableReferences().size() > 1) {
      this.assignNonDeclaredAliases(ag);
    }

  }

  public void validateTableReferences(final TableReferences tableReferences, final AliasGenerator ag) {
    if (this.baseTable != null) {
      this.baseTable.validateTableReferences(tableReferences, ag);
    }
    if (this.joins != null) {
      for (Join j : this.joins) {
        j.getTable().validateTableReferences(tableReferences, ag);
      }
    }
    if (this.wherePredicate != null) {
      this.wherePredicate.validateTableReferences(tableReferences, ag);
    }
    if (this.groupBy != null) {
      for (Expression e : this.groupBy) {
        e.validateTableReferences(tableReferences, ag);
      }
    }
    if (this.havingPredicate != null) {
      this.havingPredicate.validateTableReferences(tableReferences, ag);
    }
    if (this.combinedSelect != null) {
      this.combinedSelect.validateTableReferences(tableReferences, ag);
    }
    if (this.orderingTerms != null) {
      for (@SuppressWarnings("unused")
      OrderingTerm e : this.orderingTerms) {
        //
      }
    }

  }

  public static class TableReferences {

    private Set<DatabaseObject> tableReferences = new HashSet<DatabaseObject>();
    private Set<String> aliases = new HashSet<String>();

    public void register(final String alias, final DatabaseObject databaseObject) {
      if (this.tableReferences.contains(databaseObject)) {
        throw new InvalidLiveSQLStatementException(
            "An instance of the " + databaseObject.getType() + " " + databaseObject.renderUnescapedName()
                + (alias == null ? " (with no alias)" : " (with alias '" + alias + "')")
                + " is used multiple times in the Live SQL statement (in the FROM clause, JOIN clause, or a subquery). "
                + "If you need to include the same " + databaseObject.getType()
                + " multiple times in the query you can get more instances of it using the DAO method new"
                + SUtil.upperFirst(databaseObject.getType()) + "().");
      }
      this.tableReferences.add(databaseObject);
    }

    public Set<DatabaseObject> getTableReferences() {
      return tableReferences;
    }

    public Set<String> getAliases() {
      return aliases;
    }

    public int size() {
      return this.tableReferences.size();
    }

  }

  public static class AliasGenerator {

    private Set<String> used = new HashSet<String>();

    private char letter = 'a';
    private int seq = 0;

    public void register(final String alias, final DatabaseObject databaseObject) {
      if (alias == null) {
        return;
      }

      if (alias.isEmpty()) {
        throw new InvalidLiveSQLStatementException(
            "Empty alias found for " + databaseObject.getType().toLowerCase() + " "
                + databaseObject.renderUnescapedName() + ". Any specified alias for a table or view must be non-empty. "
                + "Use any combination of alphanumeric characters as an alias. "
                + "Usually aliases are very short, commonly a single letter.");
      }

      if (!this.used.add(alias)) {
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

  public void assignNonDeclaredAliases(final AliasGenerator ag) {
    if (this.baseTable != null) {
      this.baseTable.designateAliases(ag);
    }
    if (this.joins != null) {
      for (Join j : this.joins) {
        j.getTable().designateAliases(ag);
      }
    }
    if (this.wherePredicate != null) {
      this.wherePredicate.designateAliases(ag);
    }
    if (this.groupBy != null) {
      for (Expression e : this.groupBy) {
        e.designateAliases(ag);
      }
    }
    if (this.havingPredicate != null) {
      this.havingPredicate.designateAliases(ag);
    }
    if (this.orderingTerms != null) {
      for (@SuppressWarnings("unused")
      OrderingTerm e : this.orderingTerms) {
        //
      }
    }
  }

  protected List<Column> getColumnsField(final Object cs, final Class<?> clazz, final String colName)
      throws IllegalArgumentException, IllegalAccessException {
    Field cf = ReflectionUtils.findField(clazz, colName);
    cf.setAccessible(true);
    Object object = cf.get(cs);
    @SuppressWarnings("unchecked")
    List<Column> columns = (List<Column>) object;
    return columns;
  }

}
