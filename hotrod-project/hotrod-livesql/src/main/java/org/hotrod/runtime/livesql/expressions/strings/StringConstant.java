package org.hotrod.runtime.livesql.expressions.strings;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class StringConstant extends StringExpression {

  // Properties

  private String value;
  private boolean parameterize;

  // Constructor

  public StringConstant(final String value) {
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
      w.write("'" + this.value + "'");
    }
  }

}
