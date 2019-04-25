package sql;

import java.util.Arrays;
import java.util.List;

import com.sun.rowset.internal.Row;

import metadata.ColumnOrdering;
import sql.Select.Limit;

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
