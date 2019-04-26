package sql;

import java.util.List;

import com.sun.rowset.internal.Row;

import metadata.Column;
import metadata.ColumnOrdering;
import sql.predicates.And;
import sql.predicates.Or;
import sql.predicates.Predicate;

public class SelectWhere {

  // Properties

  private Select select;

  // Constructors

  SelectWhere(final Select select, final Predicate predicate) {
    this.select = select;
    this.select.setWhereCondition(predicate);
  }

  // Same stage

  public SelectWhere and(final Predicate predicate) {
    this.select.setWhereCondition(new And(this.select.getWhereCondition(), predicate));
    return this;
  }

  public SelectWhere or(final Predicate predicate) {
    this.select.setWhereCondition(new Or(this.select.getWhereCondition(), predicate));
    return this;
  }

  // Next stages

  public SelectGroupBy groupBy(final Column... columns) {
    return new SelectGroupBy(this.select, columns);
  }

  public SelectOrderBy orderBy(final ColumnOrdering... columnOrderings) {
    return new SelectOrderBy(this.select, columnOrderings);
  }

  public SelectOffset offset(final int offset) {
    return new SelectOffset(this.select, offset);
  }

  public SelectLimit limit(final int limit) {
    return new SelectLimit(this.select, limit);
  }

  // Execute

  public List<Row> execute() {
    return null;
  }

}
