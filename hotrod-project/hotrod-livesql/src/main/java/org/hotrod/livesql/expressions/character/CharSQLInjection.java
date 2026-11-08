package org.hotrod.livesql.expressions.character;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.queries.QueryWriter;

public class CharSQLInjection extends CharSyntaxExpression {

  public static final CharSQLInjection ZERO = new CharSQLInjection("0");

  // Properties

  private String value;

  // Constructor

  CharSQLInjection(final String value) {
    super(Expression.PRECEDENCE_LITERAL);
    this.value = value;
  }

  // Rendering

  @Override
  protected void renderTo(final QueryWriter w) {
    w.write(this.value);
  }

}
