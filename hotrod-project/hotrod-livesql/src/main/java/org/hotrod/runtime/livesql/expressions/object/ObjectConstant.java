package org.hotrod.runtime.livesql.expressions.object;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.general.Constant;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class ObjectConstant extends ObjectExpression {

  private Constant<Object> constant;

  public ObjectConstant(final Object value) {
    super(Expression.PRECEDENCE_LITERAL);
    this.constant = new Constant<Object>(value);
    super.register(this.constant);
  }

  @Override
  public void renderTo(final QueryWriter w) {
    this.constant.renderTo(w);
  }

}
