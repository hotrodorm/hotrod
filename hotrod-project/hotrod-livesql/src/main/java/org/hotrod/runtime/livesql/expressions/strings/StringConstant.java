package org.hotrod.runtime.livesql.expressions.strings;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.general.Constant;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.TableReferences;

public class StringConstant extends StringExpression {

  private Constant<String> constant;

  public StringConstant(final String value) {
    super(Expression.PRECEDENCE_LITERAL);
    this.constant = new Constant<String>(value);
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
