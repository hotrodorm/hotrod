package org.hotrod.runtime.livesql.expressions.predicates;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.general.Constant;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.TableReferences;

public class BooleanConstant extends Predicate {

  private Constant<Boolean> constant;

  public BooleanConstant(final Boolean value) {
    super(Expression.PRECEDENCE_LITERAL);
    this.constant = new Constant<Boolean>(value);
  }

  @Override
  public void renderTo(final QueryWriter w) {
    this.constant.renderTo(w);
  }

  // Validation

  @Override
  public void validateTableReferences(final TableReferences tableReferences, final AliasGenerator ag) {
    // Nothing to do. No inner queries
  }

  @Override
  public void designateAliases(final AliasGenerator ag) {
    // Nothing to do. No inner queries
  }

}
