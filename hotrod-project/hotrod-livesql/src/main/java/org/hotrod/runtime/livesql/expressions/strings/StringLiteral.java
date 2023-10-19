package org.hotrod.runtime.livesql.expressions.strings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hotrod.runtime.livesql.exceptions.InvalidLiteralException;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class StringLiteral extends StringExpression {

  private static final int MAX_LENGTH = 100; // min zero; max 100 chars

  private static Pattern VALID_PATTERN = Pattern.compile("^[ -\\~]*$"); // all ASCII from <space> to ~;

  // Properties

  private String value;

  // Constructor

  public StringLiteral(final String value) {
    super(Expression.PRECEDENCE_LITERAL);
    if (value == null) {
      throw new InvalidLiteralException("A literal String value cannot be null");
    }
    if (value.length() > MAX_LENGTH) {
      throw new InvalidLiteralException("A literal String value cannot exceed " + MAX_LENGTH + " characters");
    }
    Matcher matcher = VALID_PATTERN.matcher(value);
    if (!matcher.find()) {
      throw new InvalidLiteralException(
          "A literal String value can only have ASCII chars between <space> and tilde (~) but it's '" + value + "'");
    }
    this.value = value;
  }

  // Rendering

  @Override
  public void renderTo(final QueryWriter w) {
    w.write("'" + this.value + "'");
  }

}
