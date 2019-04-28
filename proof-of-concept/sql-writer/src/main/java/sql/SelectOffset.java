package sql;

import java.util.List;

import com.sun.rowset.internal.Row;

public class SelectOffset implements ExecutableSelect {

  // Properties

  private AbstractSelect select;

  // Constructor

  SelectOffset(final AbstractSelect select, final int offset) {
    this.select = select;
    this.select.setOffset(offset);
  }

  // Next stages

  public SelectLimit limit(final int limit) {
    return new SelectLimit(this.select, limit);
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
