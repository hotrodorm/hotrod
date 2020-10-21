package org.hotrod.runtime.livesql.expressions.predicates;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class Between extends Predicate {

  private Expression<?> value;
  private Expression<?> from;
  private Expression<?> to;

  public <T> Between(final Expression<T> value, final Expression<T> from, final Expression<T> to) {
    super(Expression.PRECEDENCE_BETWEEN);
    this.value = value;
    this.from = from;
    this.to = to;
    super.register(this.value);
    super.register(this.from);
    super.register(this.to);
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
