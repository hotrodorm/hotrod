package org.hotrod.runtime.livesql.expressions.strings;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class StringConstant extends StringExpression {

  private static final int MAX_LITERAL_STRING_LENGTH = 100;

  private static final String PRINTABLE_ASCII_PATTERN = "^[ -!#-&\\(-~]*$";

  // Properties

  private String value;
  private boolean parameterize;

  // Constructor

  public StringConstant(final String value) {
    super(Expression.PRECEDENCE_LITERAL);
//    if (value.length() > MAX_LITERAL_STRING_LENGTH) {
    this.parameterize = true;
//    } else {
//      this.parameterize = !value.matches(PRINTABLE_ASCII_PATTERN);
//    }
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
