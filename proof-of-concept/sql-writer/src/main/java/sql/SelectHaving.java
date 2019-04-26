package sql;

import java.util.List;

import com.sun.rowset.internal.Row;

import metadata.ColumnOrdering;
import sql.predicates.And;
import sql.predicates.Or;
import sql.predicates.Predicate;

public class SelectHaving {

  // Properties

  private Select select;

  // Constructor

  SelectHaving(final Select select, final Predicate predicate) {
    this.select = select;
    this.select.setHavingCondition(predicate);
  }

  // Same stage

  public SelectHaving and(final Predicate predicate) {
    this.select.setHavingCondition(new And(this.select.getHavingCondition(), this.select.getHavingCondition()));
    return this;
  }

  public SelectHaving or(final Predicate predicate) {
    this.select.setHavingCondition(new Or(this.select.getHavingCondition(), this.select.getHavingCondition()));
    return this;
  }

  // Next stages

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
