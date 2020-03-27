package org.hotrod.runtime.livesql.expressions.predicates;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.TableReferences;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class BooleanValue extends Predicate {

  private Expression<Boolean> value;

  public BooleanValue(final Expression<Boolean> value) {
    super(Expression.PRECEDENCE_LITERAL);
    this.value = value;
    super.setPrecedence(value.getPrecedence());
  }

  @Override
  public void renderTo(final QueryWriter w) {
    this.value.renderTo(w);
  }

  // Validation

  @Override
  public void validateTableReferences(final TableReferences tableReferences, final AliasGenerator ag) {
    this.value.validateTableReferences(tableReferences, ag);
  }

  @Override
  public void designateAliases(final AliasGenerator ag) {
    this.value.designateAliases(ag);
  }

}
