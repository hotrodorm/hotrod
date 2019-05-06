package org.hotrod.runtime.sql.expressions.predicates;

import org.hotrod.runtime.sql.ExecutableSelect;
import org.hotrod.runtime.sql.QueryWriter;

public class NotExists extends Predicate {

  private static final int PRECEDENCE = 2;

  private ExecutableSelect subquery;

  public NotExists(final ExecutableSelect subquery) {
    super(PRECEDENCE);
    this.subquery = subquery;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.write("not exists (\n");
    w.enterLevel();
    this.subquery.renderTo(w);
    w.exitLevel();
    w.write("\n)");
  }

}
