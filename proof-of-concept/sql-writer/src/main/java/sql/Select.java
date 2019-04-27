package sql;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.sun.rowset.internal.Row;

import metadata.TableOrView;
import sql.QueryWriter.PreparedQuery;
import sql.SQLDialect.PaginationType;
import sql.exceptions.DuplicateAliasException;
import sql.exceptions.InvalidSQLStatementException;
import sql.expressions.Expression;
import sql.expressions.OrderingTerm;
import sql.expressions.predicates.Predicate;

class Select extends Query {

  private List<Expression> queryColumns = null;
  private TableOrView baseTable = null;
  private List<Join> joins = null;
  private Predicate wherePredicate = null;
  private List<Expression> groupBy = null;
  private Predicate havingPredicate = null;
  private List<OrderingTerm> orderingTerms = null;
  private Integer offset = null;
  private Integer limit = null;

  Select(final SQLDialect sqlDialect) {
    super(sqlDialect);
  }

  // Setters

  void setQueryColumns(final List<Expression> queryColumns) {
    this.queryColumns = queryColumns;
  }

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

  private PreparedQuery prepareQuery() {

    QueryWriter w = new QueryWriter(this.sqlDialect);

    // (re)assign aliases to tables without them

    if (this.baseTable != null) {

      this.baseTable.setDesignatedAlias(null);
      for (Join j : this.joins) {
        j.getTable().setDesignatedAlias(null);
      }

      AliasGenerator ag = new AliasGenerator();
      try {
        ag.register(this.baseTable.getAlias());
      } catch (DuplicateAliasException e) {
        throw new InvalidSQLStatementException("Duplicate table or view alias '" + this.baseTable.getAlias() + "'");
      }
      for (Join j : this.joins) {
        try {
          ag.register(j.getTable().getAlias());
        } catch (DuplicateAliasException e) {
          throw new InvalidSQLStatementException("Duplicate table or view alias '" + j.getTable().getAlias() + "'");
        }
      }

      if (this.baseTable.getAlias() == null) {
        this.baseTable.setDesignatedAlias(ag.next());
      }
      for (Join j : this.joins) {
        if (j.getTable().getAlias() == null) {
          j.getTable().setDesignatedAlias(ag.next());
        }
      }

    }

    // select

    w.write("select");

    // top offset & limit

    if (this.sqlDialect.getPaginationType(this.offset, this.limit) == PaginationType.TOP) {
      w.write("\n  ");
      this.sqlDialect.renderTopPagination(this.offset, this.limit, w);
    }

    // query columns

    if (this.queryColumns == null || this.queryColumns.isEmpty()) {
      w.write("\n  *");
    } else {
      boolean first = true;
      for (Expression c : this.queryColumns) {
        if (first) {
          first = false;
        } else {
          w.write(",");
        }
        w.write("\n  ");
        c.renderTo(w);
      }
    }

    // base table

    if (this.baseTable != null) {

      w.write("\nfrom " + this.sqlDialect.renderObjectName(this.baseTable) + " " + this.baseTable.getAlias());

      // joins

      for (Join j : this.joins) {
        String joinKeywords;
        joinKeywords = this.sqlDialect.renderJoinKeywords(j);
        w.write(
            "\n" + joinKeywords + " " + this.sqlDialect.renderObjectName(j.getTable()) + " " + j.getTable().getAlias());
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
        w.write("\nwhere ");
        this.wherePredicate.renderTo(w);
      }

      // group by

      if (this.groupBy != null && !this.groupBy.isEmpty()) {
        w.write("\ngroup by ");
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
        w.write("\nhaving ");
        this.havingPredicate.renderTo(w);
      }

      // order by

      if (this.orderingTerms != null && !this.orderingTerms.isEmpty()) {
        w.write("\norder by ");
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

      if (this.sqlDialect.getPaginationType(this.offset, this.limit) == PaginationType.BOTTOM) {
        w.write("\n");
        this.sqlDialect.renderBottomPagination(this.offset, this.limit, w);
      }

    }

    // return prepared query

    return w.getPreparedQuery();

  }

  public List<Row> execute() {

    PreparedQuery q = this.prepareQuery();

    System.out.println("--- SQL ---");
    System.out.println(q.getSQL());

    System.out.println("--- Parameters ---");
    for (String name : q.getParameters().keySet()) {
      Object value = q.getParameters().get(name);
      System.out.println(" * " + name + (value == null ? "" : " (" + value.getClass().getName() + ")") + ": " + value);
    }
    System.out.println("------------------");

    return null;

  }

  static class AliasGenerator {

    private Set<String> used = new HashSet<String>();

    private char letter = 'a';
    private int seq = 0;

    public void register(final String alias) throws DuplicateAliasException {
      if (alias == null) {
        return;
      }
      if (!this.used.add(alias)) {
        throw new DuplicateAliasException();
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
