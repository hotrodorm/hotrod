package org.hotrod.runtime.livesql.expressions.numbers;

import java.math.BigInteger;

import org.hotrod.runtime.livesql.expressions.Expression;

public class IntegerLiteral extends NumericLiteral {

  // Constructor

  public IntegerLiteral(final long value) {
    super(Expression.PRECEDENCE_LITERAL);
    this.formatted = "" + value;
  }

  public IntegerLiteral(final BigInteger value) {
    super(Expression.PRECEDENCE_LITERAL);
    this.formatted = "" + value;
  }

}
