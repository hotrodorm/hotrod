package org.hotrod.runtime.livesql.expressions.predicates;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.ExecutableSelect;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class NotExists extends Predicate {

  private ExecutableSelect<?> subquery;

  public NotExists(final ExecutableSelect<?> subquery) {
    super(Expression.PRECEDENCE_EXISTS);
    this.subquery = subquery;
    super.register(this.subquery);
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
