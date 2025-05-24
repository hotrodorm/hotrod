package org.hotrod.livesql.expressions.predicates;

import org.hotrod.livesql.exceptions.LiveSQLException;
import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.livesql.queries.select.SHelper;
import org.hotrod.livesql.queries.select.Select;

public class Exists extends Predicate {

  private Select<?> subquery;

  public Exists(final Select<?> subquery) {
    super(Expression.PRECEDENCE_EXISTS);
    if (subquery == null) {
      throw new LiveSQLException("Subquery cannot be null");
    }
    this.subquery = subquery;
    super.register(this.subquery);
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.write("exists (\n");
    w.enterLevel();
    SHelper.getCombinedSelect(this.subquery).renderTo(w);
    w.exitLevel();
    w.write("\n)");
  }

}
