package sql;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import metadata.Column;
import metadata.ColumnOrdering;
import metadata.TableOrView;
import sql.SQLDialect.PaginationType;
import sql.exceptions.DuplicateAliasException;
import sql.exceptions.InvalidSQLStatementException;
import sql.predicates.Predicate;

class Select {

  private SQLDialect sqlDialect;

  private List<Column> columns = null;
  private TableOrView baseTable = null;
  private List<Join> joins = null;
  private Predicate wherePredicate = null;
  private List<Column> groupBy = null;
  private Predicate havingPredicate = null;
  private List<ColumnOrdering> columnOrderings = null;
  private Integer offset = null;
  private Integer limit = null;

  Select(final SQLDialect sqlTranslator) {
    this.sqlDialect = sqlTranslator;
  }

  // Setters

  void setColumns(final List<Column> columns) {
    this.columns = columns;
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

  void setGroupBy(final List<Column> groupBy) {
    this.groupBy = groupBy;
  }

  void setHavingCondition(final Predicate havingCondition) {
    this.havingPredicate = havingCondition;
  }

  void setColumnOrderings(List<ColumnOrdering> columnOrderings) {
    this.columnOrderings = columnOrderings;
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

  private String assemble() {

    QueryWriter w = new QueryWriter(this.sqlDialect);

    // (re)assign aliases to tables without them

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

    // select

    w.write("select");

    // top offset & limit

    if (this.sqlDialect.getPaginationType(this.offset, this.limit) == PaginationType.TOP) {
      w.write("\n  ");
      this.sqlDialect.renderTopPagination(this.offset, this.limit, w);
    }

    // columns

    if (this.columns == null || this.columns.isEmpty()) {
      w.write("\n  *");
    } else {
      boolean first = true;
      for (Column c : this.columns) {
        if (first) {
          first = false;
        } else {
          w.write(",");
        }
        w.write("\n  ");
        w.write(this.sqlDialect.renderName(c.getName()));
      }
    }

    // base table

    if (this.baseTable != null) {
      w.write("\nfrom " + this.sqlDialect.renderObjectName(this.baseTable) + " " + this.baseTable.getAlias());
    }

    // joins

    for (Join j : this.joins) {
      String joinClause;
      joinClause = this.sqlDialect.renderJoinClause(j);
      w.write(
          "\n" + joinClause + " " + this.sqlDialect.renderObjectName(this.baseTable) + " " + this.baseTable.getAlias());
      try {
        w.write(" on ");
        PredicatedJoin pj = (PredicatedJoin) j;
        pj.getJoinPredicate().renderTo(w);
      } catch (ClassCastException e) {
        // ignore and skip
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
      for (Column c : this.groupBy) {
        if (first) {
          first = false;
        } else {
          w.write(", ");
        }
        c.renderTo(w);
      }
    }

    // having

    if (this.havingPredicate != null) {
      w.write("\nhaving ");
      this.havingPredicate.renderTo(w);
    }

    // order by

    if (this.columnOrderings != null && !this.columnOrderings.isEmpty()) {
      w.write("\norder by ");
      boolean first = true;
      for (ColumnOrdering o : this.columnOrderings) {
        if (first) {
          first = false;
        } else {
          w.write(", ");
        }
        o.renderTo(w);
      }
    }

    // bottom offset & limit

    if (this.sqlDialect.getPaginationType(this.offset, this.limit) == PaginationType.BOTTOM) {
      w.write("\n");
      this.sqlDialect.renderTopPagination(this.offset, this.limit, w);
    }

    // return assembled string

    return w.toString();
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
