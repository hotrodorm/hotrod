package org.hotrod.runtime.livesql.expressions.numbers;

import org.hotrod.runtime.livesql.expressions.Expression;

public abstract class NumberExpression extends Expression<Number> {

  protected NumberExpression(final int precedence) {
    super(precedence);
  }

  // Basic arithmetic

  public NumberExpression plus(final NumberExpression n) {
    return new Plus(this, n);
  }

  public NumberExpression plus(final Number n) {
    return new Plus(this, new NumberConstant(n));
  }

  public NumberExpression minus(final NumberExpression n) {
    return new Minus(this, n);
  }

  public NumberExpression minus(final Number n) {
    return new Minus(this, new NumberConstant(n));
  }

  public NumberExpression mult(final NumberExpression n) {
    return new Mult(this, n);
  }

  public NumberExpression mult(final Number n) {
    return new Mult(this, new NumberConstant(n));
  }

  public NumberExpression div(final NumberExpression n) {
    return new Div(this, n);
  }

  public NumberExpression div(final Number n) {
    return new Div(this, new NumberConstant(n));
  }

  public NumberExpression remainder(final NumberExpression n) {
    return new Remainder(this, n);
  }

  public NumberExpression remainder(final Number n) {
    return new Remainder(this, new NumberConstant(n));
  }

  public NumberExpression pow(final NumberExpression exponent) {
    return new Power(this, exponent);
  }

  public NumberExpression pow(final Number exponent) {
    return new Power(this, new NumberConstant(exponent));
  }

  public NumberExpression log(final NumberExpression base) {
    return new Log(this, base);
  }

  public NumberExpression log(final Number base) {
    return new Log(this, new NumberConstant(base));
  }

  public NumberExpression round() {
    return new Round(this, new NumberConstant(0));
  }

  public NumberExpression round(final NumberExpression places) {
    return new Round(this, places);
  }

  public NumberExpression round(final Number places) {
    return new Round(this, new NumberConstant(places));
  }

  public NumberExpression trunc() {
    return new Trunc(this, new NumberConstant(0));
  }

  public NumberExpression trunc(final NumberExpression places) {
    return new Trunc(this, places);
  }

  public NumberExpression trunc(final Number places) {
    return new Trunc(this, new NumberConstant(places));
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
