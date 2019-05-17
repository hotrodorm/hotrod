package org.hotrod.runtime.livesql.expressions.object;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.general.Constant;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.TableReferences;

public class ObjectConstant extends ObjectExpression {

  private Constant<Object> constant;

  public ObjectConstant(final Object value) {
    super(Expression.PRECEDENCE_LITERAL);
    this.constant = new Constant<Object>(value);
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
