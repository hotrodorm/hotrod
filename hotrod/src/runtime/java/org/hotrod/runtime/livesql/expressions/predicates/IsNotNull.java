package org.hotrod.runtime.livesql.expressions.predicates;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.TableReferences;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class IsNotNull extends Predicate {

  private Expression<?> a;

  public IsNotNull(final Expression<?> a) {
    super(Expression.PRECEDENCE_IS_NULL);
    this.a = a;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    super.renderInner(this.a, w);
    w.write(" is not null");
  }

  // Validation

  @Override
  public void validateTableReferences(final TableReferences tableReferences, final AliasGenerator ag) {
    this.a.validateTableReferences(tableReferences, ag);
  }

  @Override
  public void designateAliases(final AliasGenerator ag) {
    this.a.designateAliases(ag);
  }

}
