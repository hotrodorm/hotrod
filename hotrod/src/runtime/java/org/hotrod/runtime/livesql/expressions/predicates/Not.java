package org.hotrod.runtime.livesql.expressions.predicates;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.TableReferences;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class Not extends Predicate {

  private Expression<Boolean> a;

  public Not(final Expression<Boolean> a) {
    super(Expression.PRECEDENCE_NOT);
    this.a = a;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.write("not ");
    super.renderInner(this.a, w);
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
