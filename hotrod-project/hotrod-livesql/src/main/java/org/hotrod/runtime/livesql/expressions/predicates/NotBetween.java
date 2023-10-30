package org.hotrod.runtime.livesql.expressions.predicates;

import org.hotrod.runtime.livesql.expressions.ComparableExpression;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class NotBetween<T extends ComparableExpression> extends Predicate {

  private T value;
  private T from;
  private T to;

  public NotBetween(final T value, final T from, final T to) {
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
    w.write("not between ");
    super.renderInner(this.from, w);
    w.write(" and ");
    super.renderInner(this.to, w);
  }

}
