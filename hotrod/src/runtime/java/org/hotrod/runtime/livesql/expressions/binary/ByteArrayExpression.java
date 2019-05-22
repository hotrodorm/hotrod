package org.hotrod.runtime.livesql.expressions.binary;

import org.hotrod.runtime.livesql.expressions.Expression;

public abstract class ByteArrayExpression extends Expression<byte[]> {

  protected ByteArrayExpression(final int precedence) {
    super(precedence);
  }

  // Coalesce

  public ByteArrayExpression coalesce(final ByteArrayExpression a) {
    return new ByteArrayCoalesce(this, a);
  }

  public ByteArrayExpression coalesce(final byte[] a) {
    return new ByteArrayCoalesce(this, new ByteArrayConstant(a));
  }

}
