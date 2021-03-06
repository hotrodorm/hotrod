package org.hotrod.runtime.livesql.expressions.predicates;

import org.hotrod.runtime.livesql.ExecutableSelect;
import org.hotrod.runtime.livesql.QueryWriter;

public class Exists extends Predicate {

  private static final int PRECEDENCE = 2;

  private ExecutableSelect subquery;

  public Exists(final ExecutableSelect subquery) {
    super(PRECEDENCE);
    this.subquery = subquery;
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
