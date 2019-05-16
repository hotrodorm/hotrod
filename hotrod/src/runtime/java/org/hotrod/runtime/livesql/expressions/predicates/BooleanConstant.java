package org.hotrod.runtime.livesql.expressions.predicates;

import org.hotrod.runtime.livesql.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.AbstractSelect.TableReferences;
import org.hotrod.runtime.livesql.QueryWriter;
import org.hotrod.runtime.livesql.expressions.Constant;
import org.hotrod.runtime.livesql.expressions.Expression;

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
