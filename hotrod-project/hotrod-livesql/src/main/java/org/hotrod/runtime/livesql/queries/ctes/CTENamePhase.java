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

  public CTEHeaderPhase columnNames(final String name, final String... columnNames) {
    String[] all = new String[columnNames.length + 1];
    all[0] = name;
    for (int i = 0; i < columnNames.length; i++) {
      all[i + 1] = columnNames[i];
    }
    return new CTEHeaderPhase(this.name, all);
  }

  public CTE as(ExecutableSelect<Row> select) {
    return new CTE(this.name, select);
  }

}
