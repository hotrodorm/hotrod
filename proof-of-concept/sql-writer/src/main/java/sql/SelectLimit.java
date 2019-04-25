package sql;

import java.util.List;

import com.sun.rowset.internal.Row;

import sql.Select.Limit;

public class SelectLimit {

  // Properties

  private Select select;

  // Constructor

  SelectLimit(final Select select, final Limit limit) {
    this.select = select;
    this.select.setLimit(limit);
  }

  // Execute

  public List<Row> execute() {
    return null;
  }

}
