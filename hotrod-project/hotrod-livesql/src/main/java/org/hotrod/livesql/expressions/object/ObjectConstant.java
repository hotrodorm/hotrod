package org.hotrod.livesql.expressions.object;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.livesql.queries.SQLParameterWriter.RenderedParameter;

public class ObjectConstant extends ObjectExpression {

  private Object value;
  private boolean parameterize;

  // Constructor

  public ObjectConstant(final Object value) {
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
      if (this.value instanceof String) {
        w.write("'" + this.value + "'");
      } else {
        w.write("" + this.value);
      }
    }
  }

}
