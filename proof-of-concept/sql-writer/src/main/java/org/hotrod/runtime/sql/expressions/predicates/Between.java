package org.hotrod.runtime.sql.expressions.predicates;

import org.hotrod.runtime.sql.QueryWriter;
import org.hotrod.runtime.sql.expressions.Expression;

public class Between extends Predicate {

  private static final int PRECEDENCE = 6;

  private Expression<?> value;
  private Expression<?> from;
  private Expression<?> to;

  public <T> Between(final Expression<T> value, final Expression<T> from, final Expression<T> to) {
    super(PRECEDENCE);
    this.value = value;
    this.from = from;
    this.to = to;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    super.renderInner(this.value, w);
    w.write(" between ");
    super.renderInner(this.from, w);
    w.write(" and ");
    super.renderInner(this.to, w);
  }

}
