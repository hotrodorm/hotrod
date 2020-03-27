package org.hotrod.runtime.livesql.expressions.strings;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.TableReferences;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class StringValue extends StringExpression {

  private Expression<String> value;

  public StringValue(final Expression<String> value) {
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
