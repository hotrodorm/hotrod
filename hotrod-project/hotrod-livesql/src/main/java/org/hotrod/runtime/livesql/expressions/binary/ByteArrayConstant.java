package org.hotrod.runtime.livesql.expressions.binary;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.general.Constant;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class ByteArrayConstant extends ByteArrayExpression {

  private Constant<byte[]> constant;

  public ByteArrayConstant(final byte[] value) {
    super(Expression.PRECEDENCE_LITERAL);
    this.constant = new Constant<byte[]>(value);
    super.register(this.constant);
  }

  @Override
  public void renderTo(final QueryWriter w) {
    this.constant.renderTo(w);
  }

}
