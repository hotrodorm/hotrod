package org.hotrod.runtime.sql.expressions.numbers;

import org.hotrod.runtime.sql.expressions.Expression;

public abstract class NumberExpression extends Expression<Number> {

  protected NumberExpression(final int precedence) {
    super(precedence);
  }

  // Basic arithmetic

  public NumberExpression plus(final NumberExpression n) {
    return new Plus(this, n);
  }

  public NumberExpression minus(final NumberExpression n) {
    return new Minus(this, n);
  }

  public NumberExpression mult(final NumberExpression n) {
    return new Mult(this, n);
  }

  public NumberExpression div(final NumberExpression n) {
    return new Div(this, n);
  }

  public NumberExpression remainder(final NumberExpression n) {
    return new Remainder(this, n);
  }

  public NumberExpression pow(final NumberExpression exponent) {
    return new Pow(this, exponent);
  }

  public NumberExpression log(final NumberExpression base) {
    return new Log(this, base);
  }

  public NumberExpression round(final NumberExpression n) {
    return new Round(this, n);
  }

  public NumberExpression neg() {
    return new Neg(this);
  }

  public NumberExpression abs() {
    return new Abs(this);
  }

  public NumberExpression signum() {
    return new Signum(this);
  }

}
