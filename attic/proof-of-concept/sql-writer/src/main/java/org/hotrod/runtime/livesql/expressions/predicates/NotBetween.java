package org.hotrod.runtime.livesql.expressions.predicates;

import org.hotrod.runtime.livesql.QueryWriter;
import org.hotrod.runtime.livesql.expressions.Expression;

public class NotBetween extends Predicate {

  private static final int PRECEDENCE = 6;

  private Expression<?> value;
  private Expression<?> from;
  private Expression<?> to;

  public <T> NotBetween(final Expression<T> value, final Expression<T> from, final Expression<T> to) {
    super(PRECEDENCE);
    this.value = value;
    this.from = from;
    this.to = to;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    super.renderInner(this.value, w);
    w.write("not between ");
    super.renderInner(this.from, w);
    w.write(" and ");
    super.renderInner(this.to, w);
  }

}
