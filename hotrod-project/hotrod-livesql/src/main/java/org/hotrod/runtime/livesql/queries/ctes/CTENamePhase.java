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

  public CTEHeaderPhase columnNames(final String first, final String... rest) {
    String[] all = new String[rest.length + 1];
    all[0] = first;
    for (int i = 0; i < rest.length; i++) {
      all[i + 1] = rest[i];
    }
    return new CTEHeaderPhase(this.name, all);
  }

  public CTE as(ExecutableSelect<Row> select) {
    return new CTE(this.name, select);
  }

}
