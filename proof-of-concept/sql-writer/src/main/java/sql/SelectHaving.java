package sql;

import java.util.List;

import com.sun.rowset.internal.Row;

import metadata.ColumnOrdering;
import sql.Select.Limit;
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
    this.select.setHavingCondition(Predicate.and(this.select.getHavingCondition(), this.select.getHavingCondition()));
    return this;
  }

  public SelectHaving or(final Predicate predicate) {
    this.select.setHavingCondition(Predicate.or(this.select.getHavingCondition(), this.select.getHavingCondition()));
    return this;
  }

  // Next stages

  public SelectOrderBy orderBy(final ColumnOrdering... columnOrderings) {
    return new SelectOrderBy(this.select, columnOrderings);
  }

  public SelectLimit limit(final int limit) {
    return new SelectLimit(this.select, new Limit(0, limit));
  }

  public SelectLimit limit(final int offset, final int limit) {
    return new SelectLimit(this.select, new Limit(offset, limit));
  }

  // Execute

  public List<Row> execute() {
    return null;
  }

}
