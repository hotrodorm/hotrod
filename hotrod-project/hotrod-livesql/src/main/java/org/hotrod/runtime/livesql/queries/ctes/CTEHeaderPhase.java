package org.hotrod.runtime.livesql.queries.ctes;

import org.hotrod.runtime.livesql.Row;
import org.hotrod.runtime.livesql.queries.select.ExecutableSelect;

public class CTEHeaderPhase {

  // Properties

  private String name;
  private String[] columnNames;

  // Constructor

  public CTEHeaderPhase(final String name, final String[] columnNames) {
    this.name = name;
    this.columnNames = columnNames;
  }

  // Next stages

  public CTE as(ExecutableSelect<Row> select) {
    return new CTE(this.name, this.columnNames, select);
  }

}
