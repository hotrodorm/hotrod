package org.hotrod.runtime.livesql.expressions.numbers;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.general.Constant;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class NumberConstant extends NumberExpression {

  private Constant<Number> constant;

  public NumberConstant(final Number value) {
    super(Expression.PRECEDENCE_LITERAL);
    this.constant = new Constant<Number>(value);
    super.register(this.constant);
  }

  @Override
  public void renderTo(final QueryWriter w) {
    this.constant.renderTo(w);
  }

}
