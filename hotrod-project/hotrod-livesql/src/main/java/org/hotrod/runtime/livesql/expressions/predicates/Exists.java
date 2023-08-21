package org.hotrod.runtime.livesql.expressions.predicates;

import org.hotrod.runtime.livesql.exceptions.LiveSQLException;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.ExecutableSelect;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class Exists extends Predicate {

  private ExecutableSelect<?> subquery;

  public Exists(final ExecutableSelect<?> subquery) {
    super(Expression.PRECEDENCE_EXISTS);
    if (subquery == null) {
      throw new LiveSQLException("Subquery cannot be null");
    }
    this.subquery = subquery;
    super.register(this.subquery);
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.write("exists (\n");
    w.enterLevel();
    this.subquery.renderTo(w);
    w.exitLevel();
    w.write("\n)");
  }

}
