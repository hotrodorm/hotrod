package org.hotrod.livesql.expressions.bool;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.queries.QueryWriter;

public class NotBetween<T extends ComparableExpression> extends BooleanSyntaxExpression {

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
  protected void renderTo(final QueryWriter w) {
    super.renderInner(this.value, w);
    w.write("not between ");
    super.renderInner(this.from, w);
    w.write(" and ");
    super.renderInner(this.to, w);
  }

}
