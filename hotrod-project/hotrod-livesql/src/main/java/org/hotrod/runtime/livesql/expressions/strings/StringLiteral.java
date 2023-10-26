package org.hotrod.runtime.livesql.expressions.strings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hotrod.runtime.livesql.exceptions.InvalidLiteralException;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class StringLiteral extends StringExpression {

  private static final String RULE = "Formally, a LiveSQL String literal must be a non-null value "
      + "and can only include ASCII chars between <space> and tilde (~).";

  private static Pattern VALID_PATTERN = Pattern.compile("^[ -\\~]*$"); // all ASCII from <space> to ~

  // Properties

  private String value;

  // Constructor

  public StringLiteral(final String value) {
    super(Expression.PRECEDENCE_LITERAL);
    if (value == null) {
      throw new InvalidLiteralException("A literal String value cannot be null. " + RULE);
    }
    Matcher matcher = VALID_PATTERN.matcher(value);
    if (!matcher.find()) {
      throw new InvalidLiteralException(
          "A literal String value can only include ASCII chars between <space> and tilde (~) but it's '" + value + "'. "
              + RULE);
    }
    this.value = value.replace("'", "''");
  }

  // Rendering

  @Override
  public void renderTo(final QueryWriter w) {
    w.write("'" + this.value + "'");
  }

}
