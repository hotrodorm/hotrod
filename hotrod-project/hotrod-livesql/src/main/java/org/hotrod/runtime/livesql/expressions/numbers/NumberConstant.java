package org.hotrod.runtime.livesql.expressions.numbers;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.QueryWriter;
import org.hotrod.runtime.livesql.queries.SQLParameterWriter.RenderedParameter;

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
  public void renderTo(final QueryWriter w) {
    if (this.parameterize) {
      RenderedParameter p = w.registerParameter(this.value);
      w.write(p.getPlaceholder());
    } else {
      w.write("" + this.value);
    }
  }

}
