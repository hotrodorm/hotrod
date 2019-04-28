package sql;

import java.util.List;

import com.sun.rowset.internal.Row;

public class SelectLimit implements ExecutableSelect {

  // Properties

  private AbstractSelect select;

  // Constructor

  SelectLimit(final AbstractSelect select, final int limit) {
    this.select = select;
    this.select.setLimit(limit);
  }

  // Rendering

  @Override
  public void renderTo(final QueryWriter w) {
    this.select.renderTo(w);
  }

  // Execute

  public List<Row> execute() {
    return this.select.execute();
  }

}
