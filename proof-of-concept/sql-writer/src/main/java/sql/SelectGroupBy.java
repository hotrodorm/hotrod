package sql;

import java.util.Arrays;
import java.util.List;

import com.sun.rowset.internal.Row;

import metadata.Column;
import metadata.ColumnOrdering;
import sql.predicates.Predicate;

public class SelectGroupBy {

  // Properties

  private Select select;

  // Constructor

  SelectGroupBy(final Select select, final Column... columns) {
    this.select = select;
    this.select.setGroupBy(Arrays.asList(columns));
  }

  // Next stages

  public SelectHaving having(final Predicate predicate) {
    return new SelectHaving(this.select, predicate);
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
