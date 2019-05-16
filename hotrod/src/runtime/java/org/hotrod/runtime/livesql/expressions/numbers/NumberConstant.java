package org.hotrod.runtime.livesql.expressions.numbers;

import org.hotrod.runtime.livesql.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.AbstractSelect.TableReferences;
import org.hotrod.runtime.livesql.QueryWriter;
import org.hotrod.runtime.livesql.expressions.Constant;
import org.hotrod.runtime.livesql.expressions.Expression;

public class NumberConstant extends NumberExpression {

  private Constant<Number> constant;

  public NumberConstant(final Number value) {
    super(Expression.PRECEDENCE_LITERAL);
    this.constant = new Constant<Number>(value);
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
