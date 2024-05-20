package org.hotrod.runtime.livesql.expressions.predicates;

import java.util.List;

import org.hotrod.runtime.livesql.expressions.ComparableExpression;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.QueryWriter;
import org.hotrodorm.hotrod.utils.Separator;

public class InList<T extends ComparableExpression> extends Predicate {

  private T value;
  private List<T> expressions;

  public InList(final T value, final List<T> list) {
    super(Expression.PRECEDENCE_IN);
    this.value = value;
    this.expressions = list;
    super.register(this.value);
    this.expressions.forEach(e -> super.register(e));
  }

  @Override
  public void renderTo(final QueryWriter w) {
    super.renderInner(value, w);
    w.write(" in (");
    Separator sep = new Separator();
    for (T e : this.expressions) {
      w.write(sep.render());
      super.renderInner(e, w);
    }
    w.write(")");
  }

}
