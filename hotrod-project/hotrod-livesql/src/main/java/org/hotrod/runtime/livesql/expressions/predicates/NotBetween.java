package org.hotrod.runtime.livesql.expressions.predicates;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.TableReferences;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class NotBetween extends Predicate {

  private Expression<?> value;
  private Expression<?> from;
  private Expression<?> to;

  public <T> NotBetween(final Expression<T> value, final Expression<T> from, final Expression<T> to) {
    super(Expression.PRECEDENCE_BETWEEN);
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

  // Validation

  @Override
  public void validateTableReferences(final TableReferences tableReferences, final AliasGenerator ag) {
    this.value.validateTableReferences(tableReferences, ag);
    this.from.validateTableReferences(tableReferences, ag);
    this.to.validateTableReferences(tableReferences, ag);
  }

  @Override
  public void designateAliases(final AliasGenerator ag) {
    this.value.designateAliases(ag);
    this.from.designateAliases(ag);
    this.to.designateAliases(ag);
  }

}
