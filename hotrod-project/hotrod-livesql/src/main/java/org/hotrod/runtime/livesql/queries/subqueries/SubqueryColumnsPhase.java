package org.hotrod.runtime.livesql.queries.subqueries;

import org.hotrod.runtime.livesql.queries.select.ExecutableSelect;

@SuppressWarnings("deprecation")
public class SubqueryColumnsPhase {

  private String name;
  private String[] columns;

  public SubqueryColumnsPhase(String name, String[] columns) {
    this.name = name;
    this.columns = columns;
  }

  public Subquery as(final ExecutableSelect<?> select) {
    return new Subquery(name, columns, select);

  }

}
