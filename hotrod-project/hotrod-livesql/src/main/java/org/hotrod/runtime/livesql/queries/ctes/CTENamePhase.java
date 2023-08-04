package org.hotrod.runtime.livesql.queries.ctes;

import org.hotrod.runtime.livesql.Row;
import org.hotrod.runtime.livesql.queries.select.ExecutableSelect;

public class CTENamePhase {

  // Properties

  private String name;

  // Constructor

  public CTENamePhase(final String cteName) {
    this.name = cteName;
  }

  // Next stages

  public CTEHeaderPhase columnNames(final String... columnNames) {
    return new CTEHeaderPhase(this.name, columnNames);
  }

  public CTE as(ExecutableSelect<Row> select) {
    return new CTE(this.name, select);
  }

}
