package org.hotrod.runtime.livesql.expressions.binary;

import org.hotrod.runtime.livesql.expressions.Expression;

public abstract class ByteArrayExpression extends Expression<byte[]> {

  protected ByteArrayExpression(final int precedence) {
    super(precedence);
  }

}
