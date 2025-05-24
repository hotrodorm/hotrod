package org.hotrod.livesql.expressions.numbers;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.livesql.queries.SQLParameterWriter.RenderedParameter;

public class NumberConstant extends NumberExpression {

  // Properties

  private Number value;
  private boolean parameterize;

  // Constructor

  public NumberConstant(final Number value) {
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
