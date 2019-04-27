package sql;

import java.util.List;

import com.sun.rowset.internal.Row;

public class SelectLimit {

  // Properties

  private Select select;

  // Constructor

  SelectLimit(final Select select, final int limit) {
    this.select = select;
    this.select.setLimit(limit);
  }

  // Execute

  public List<Row> execute() {
    return this.select.execute();
  }

}
