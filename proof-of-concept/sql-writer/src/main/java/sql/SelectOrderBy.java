package sql;

import java.util.Arrays;
import java.util.List;

import com.sun.rowset.internal.Row;

import metadata.ColumnOrdering;

public class SelectOrderBy {

  // Properties

  private Select select;

  // Constructor

  SelectOrderBy(final Select select, final ColumnOrdering... columnOrderings) {
    this.select = select;
    this.select.setColumnOrderings(Arrays.asList(columnOrderings));
  }

  // Same stage

  // Next stages

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
