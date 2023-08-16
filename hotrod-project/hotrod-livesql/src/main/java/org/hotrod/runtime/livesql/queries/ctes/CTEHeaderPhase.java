package org.hotrod.runtime.livesql.queries.ctes;

import org.hotrod.runtime.livesql.queries.select.ExecutableSelect;

public class CTEHeaderPhase {

  // Properties

  private String name;
  private String[] aliases;

  // Constructor

  public CTEHeaderPhase(final String name, final String[] aliases) {
    this.name = name;
    this.aliases = aliases;
  }

  // Next stages

  public CTE as(ExecutableSelect<?> select) {
    return new CTE(this.name, this.aliases, select);
  }

}
