package org.hotrod.runtime.sql;

import java.util.List;
import java.util.Map;

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

  public List<Map<String, Object>> execute() {
    return this.select.execute();
  }

}
