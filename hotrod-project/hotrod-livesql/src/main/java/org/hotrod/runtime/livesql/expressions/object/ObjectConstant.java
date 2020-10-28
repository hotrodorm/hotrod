package org.hotrod.runtime.livesql.expressions.object;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

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
  public void renderTo(final QueryWriter w) {
    if (this.parameterize) {
      String name = w.registerParameter(this.value);
      w.write("#{" + name);
      w.write("}");
    } else {
      if (this.value instanceof String) {
        w.write("'" + this.value + "'");
      } else {
        w.write("" + this.value);
      }
    }
  }

}
