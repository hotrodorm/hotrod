package org.hotrod.runtime.livesql.expressions.predicates;

import java.util.List;

import org.hotrod.runtime.livesql.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.QueryWriter;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.util.Separator;

public class NotInList<T> extends Predicate {

  private Expression<T> value;
  private List<Expression<T>> expressions;

  public NotInList(final Expression<T> value, final List<Expression<T>> list) {
    super(Expression.PRECEDENCE_IN);
    this.value = value;
    this.expressions = list;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    super.renderInner(value, w);
    w.write(" not in (");
    Separator sep = new Separator();
    for (Expression<T> e : this.expressions) {
      w.write(sep.render());
      super.renderInner(e, w);
    }
    w.write(")");
  }

  // Apply aliases

  @Override
  public void gatherAliases(final AliasGenerator ag) {
    for (Expression<T> e : this.expressions) {
      e.gatherAliases(ag);
    }
  }

  @Override
  public void designateAliases(final AliasGenerator ag) {
    for (Expression<T> e : this.expressions) {
      e.designateAliases(ag);
    }
  }

}
