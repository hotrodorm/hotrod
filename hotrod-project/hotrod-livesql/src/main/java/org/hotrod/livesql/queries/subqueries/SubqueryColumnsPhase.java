package org.hotrod.livesql.queries.subqueries;

import org.hotrod.livesql.queries.select.Select;

public class SubqueryColumnsPhase {

  private String name;
  private String[] columns;

  public SubqueryColumnsPhase(String name, String[] columns) {
    this.name = name;
    this.columns = columns;
  }

  public Subquery as(final Select<?> select) {
    return new Subquery(name, columns, select);

  }

}
