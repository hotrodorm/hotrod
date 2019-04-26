package sql;

import java.util.List;

import com.sun.rowset.internal.Row;

public class SelectOffset {

  // Properties

  private Select select;

  // Constructor

  SelectOffset(final Select select, final int offset) {
    this.select = select;
    this.select.setOffset(offset);
  }

  // Next stages

  public SelectLimit limit(final int limit) {
    return new SelectLimit(this.select, limit);
  }

  // Execute

  public List<Row> execute() {
    return null;
  }

}
