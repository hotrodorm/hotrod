package sql;

import java.util.ArrayList;
import java.util.List;

import metadata.Column;
import metadata.ColumnOrdering;
import metadata.TableOrView;
import sql.predicates.Predicate;

class Select {

  private List<Column> columns = null;
  private TableOrView baseTable = null;
  private List<AbstractJoin> joins = null;
  private Predicate whereCondition = null;
  private List<Column> groupBy = null;
  private Predicate havingCondition = null;
  private List<ColumnOrdering> columnOrderings = null;
  private Limit limit = null;

  static class Limit {

    private int offset;
    private int limit;

    public Limit(final int offset, final int limit) {
      super();
      this.offset = offset;
      this.limit = limit;
    }

    int getOffset() {
      return offset;
    }

    int getLimit() {
      return limit;
    }

  }
  // Setters

  void setColumns(final List<Column> columns) {
    this.columns = columns;
  }

  void setBaseTable(final TableOrView baseTable) {
    this.baseTable = baseTable;
    this.joins = new ArrayList<AbstractJoin>();
  }

  void addJoin(final AbstractJoin join) {
    this.joins.add(join);
  }

  void setWhereCondition(final Predicate whereCondition) {
    this.whereCondition = whereCondition;
  }

  void setGroupBy(final List<Column> groupBy) {
    this.groupBy = groupBy;
  }

  void setHavingCondition(final Predicate havingCondition) {
    this.havingCondition = havingCondition;
  }

  void setColumnOrderings(List<ColumnOrdering> columnOrderings) {
    this.columnOrderings = columnOrderings;
  }

  void setLimit(final Limit limit) {
    this.limit = limit;
  }

  // Getters

  Predicate getWhereCondition() {
    return whereCondition;
  }

  Predicate getHavingCondition() {
    return havingCondition;
  }
  
  // Execute
  
  private String assemble() {
    return null;
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

