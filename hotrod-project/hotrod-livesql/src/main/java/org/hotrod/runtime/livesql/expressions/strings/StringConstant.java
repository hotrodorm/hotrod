package org.hotrod.runtime.livesql.expressions.strings;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class StringConstant extends StringExpression {

  private static final int MAX_LITERAL_STRING_LENGTH = 250;

  // Control characters (x01 - x1f)
  // Single quotes
  // Delete (x7f)
  // Control characters (x80 - x9f)
  // Double quotes
  private static final String SQL_INJECTION_PATTERN = ".*[\\x01-\\x1f'\\x7f-\\x9f\"].*";

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
