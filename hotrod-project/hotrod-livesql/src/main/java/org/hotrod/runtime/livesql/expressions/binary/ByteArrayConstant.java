package org.hotrod.runtime.livesql.expressions.binary;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.QueryWriter;
import org.hotrod.runtime.livesql.queries.SQLParameterWriter.RenderedParameter;

public class ByteArrayConstant extends ByteArrayExpression {

  // Properties

  private byte[] value;
  private boolean parameterize;

  // Constructor

  public ByteArrayConstant(final byte[] value) {
    super(Expression.PRECEDENCE_LITERAL);
    this.parameterize = true;
    this.value = value;
  }

  // Rendering

  @Override
  protected void renderTo(final QueryWriter w) {
    if (this.parameterize) {
      RenderedParameter p = w.registerParameter(this.value);
      w.write(p.getPlaceholder());
    } else {
      w.write("" + this.value);
    }
  }

}
