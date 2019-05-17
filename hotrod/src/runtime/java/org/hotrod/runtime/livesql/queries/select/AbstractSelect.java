package org.hotrod.runtime.livesql.queries.select;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.session.SqlSession;
import org.hotrod.runtime.livesql.dialects.PaginationRenderer.PaginationType;
import org.hotrod.runtime.livesql.dialects.SQLDialect;
import org.hotrod.runtime.livesql.exceptions.InvalidLiveSQLStatementException;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.metadata.DatabaseObject;
import org.hotrod.runtime.livesql.metadata.TableOrView;
import org.hotrod.runtime.livesql.ordering.OrderingTerm;
import org.hotrod.runtime.livesql.queries.select.QueryWriter.LiveSQL;
import org.hotrod.runtime.util.SUtils;

public abstract class AbstractSelect<R> extends Query {

  public static final String LIVE_SQL_MAPPER_NAMESPACE = "livesql";
  public static final String LIVE_SQL_MAPPER_STATEMENT_NAME = "select";
  private static final String LIVE_SQL_MAPPER_STATEMENT = LIVE_SQL_MAPPER_NAMESPACE + "." + LIVE_SQL_MAPPER_STATEMENT_NAME;

  private boolean distinct;
  private TableOrView baseTable = null;
  private List<Join> joins = null;
  private Predicate wherePredicate = null;
  private List<Expression<?>> groupBy = null;
  private Predicate havingPredicate = null;
  private List<OrderingTerm> orderingTerms = null;
  private Integer offset = null;
  private Integer limit = null;

  private SqlSession sqlSession;
  private String mapperStatement;

  AbstractSelect(final SQLDialect sqlDialect, final boolean distinct, final SqlSession sqlSession,
      final String mapperStatement) {
    super(sqlDialect);
    this.distinct = distinct;
    this.sqlSession = sqlSession;
    this.mapperStatement = (mapperStatement == null ? LIVE_SQL_MAPPER_STATEMENT : mapperStatement);
  }

  protected abstract void writeColumns(QueryWriter w);

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

  void setGroupBy(final List<Expression<?>> groupBy) {
    this.groupBy = groupBy;
  }

  void setHavingCondition(final Predicate havingCondition) {
    this.havingPredicate = havingCondition;
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

  private LiveSQL prepareQuery() {
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

    this.writeColumns(w);

    // base table

    if (this.baseTable != null) {

      w.write("\nFROM " + this.sqlDialect.getIdentifierRenderer().renderSQLObjectName(this.baseTable)
          + (this.baseTable.getAlias() == null ? "" : " " + this.baseTable.getAlias()));

      // joins

      for (Join j : this.joins) {
        String joinKeywords;
        joinKeywords = this.sqlDialect.getJoinRenderer().renderJoinKeywords(j);
        w.write("\n" + joinKeywords + " " + this.sqlDialect.getIdentifierRenderer().renderSQLObjectName(j.getTable())
            + (j.getTable().getAlias() == null ? "" : " " + j.getTable().getAlias()));
        try {
          w.write(" on ");
          PredicatedJoin pj = (PredicatedJoin) j;
          pj.getJoinPredicate().renderTo(w);
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
        for (Expression<?> expr : this.groupBy) {
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

      // order by

      if (this.orderingTerms != null && !this.orderingTerms.isEmpty()) {
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

      // bottom offset & limit

      if ((this.offset != null || this.limit != null) && paginationType == PaginationType.BOTTOM) {
        this.sqlDialect.getPaginationRenderer().renderBottomPagination(this.offset, this.limit, w);
      }

      // enclosing pagination - end

      if ((this.offset != null || this.limit != null) && paginationType == PaginationType.ENCLOSE) {
        this.sqlDialect.getPaginationRenderer().renderEndEnclosingPagination(this.offset, this.limit, w);
      }

    }
  }

  public List<R> execute() {

    LiveSQL q = this.prepareQuery();

    System.out.println("--- SQL ---");
    System.out.println(q.getSQL());

    System.out.println("--- Parameters ---");
    for (String name : q.getParameters().keySet()) {
      Object value = q.getParameters().get(name);
      System.out.println(" * " + name + (value == null ? "" : " (" + value.getClass().getName() + ")") + ": " + value);
    }
    System.out.println("------------------");

    List<R> rows = this.sqlSession.selectList(this.mapperStatement, q.getConsolidatedParameters());

    return rows;

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
      for (Expression<?> e : this.groupBy) {
        e.validateTableReferences(tableReferences, ag);
      }
    }
    if (this.havingPredicate != null) {
      this.havingPredicate.validateTableReferences(tableReferences, ag);
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
            SUtils.upperFirst(databaseObject.getType()) + " " + databaseObject.renderUnescapedName()
                + (alias == null ? " (with no alias)" : " (with alias '" + alias + "')")
                + " is used multiple times in the Live SQL statement. "
                + "Every table or view can only be used once in the from() or join() methods of a Live SQL statement.");
      }
      this.tableReferences.add(databaseObject);
    }

    public Set<DatabaseObject> getTableReferences() {
      return tableReferences;
    }

    public Set<String> getAliases() {
      return aliases;
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
      for (Expression<?> e : this.groupBy) {
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

}

//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
