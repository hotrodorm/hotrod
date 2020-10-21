package org.hotrod.runtime.livesql.expressions.strings;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.general.Constant;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class StringConstant extends StringExpression {

  private Constant<String> constant;

  public StringConstant(final String value) {
    super(Expression.PRECEDENCE_LITERAL);
    this.constant = new Constant<String>(value);
    super.register(this.constant);
  }

  @Override
  public void renderTo(final QueryWriter w) {
    this.constant.renderTo(w);
  }

}
