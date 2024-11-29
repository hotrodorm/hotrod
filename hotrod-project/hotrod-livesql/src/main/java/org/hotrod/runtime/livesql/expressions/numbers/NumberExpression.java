package org.hotrod.runtime.livesql.expressions.numbers;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hotrod.runtime.livesql.expressions.ComparableExpression;
import org.hotrod.runtime.livesql.expressions.predicates.Between;
import org.hotrod.runtime.livesql.expressions.predicates.BooleanFreeExpression;
import org.hotrod.runtime.livesql.expressions.predicates.Equal;
import org.hotrod.runtime.livesql.expressions.predicates.GreaterThan;
import org.hotrod.runtime.livesql.expressions.predicates.GreaterThanOrEqualTo;
import org.hotrod.runtime.livesql.expressions.predicates.InList;
import org.hotrod.runtime.livesql.expressions.predicates.LessThan;
import org.hotrod.runtime.livesql.expressions.predicates.LessThanOrEqualTo;
import org.hotrod.runtime.livesql.expressions.predicates.NotBetween;
import org.hotrod.runtime.livesql.expressions.predicates.NotEqual;
import org.hotrod.runtime.livesql.expressions.predicates.NotInList;
import org.hotrod.runtime.livesql.util.BoxUtil;

public abstract class NumberExpression extends ComparableExpression {

  protected NumberExpression(final int precedence) {
    super(precedence);
  }

  // Coalesce

  public NumberFreeExpression coalesce(final NumberExpression a) {
    return new NumberCoalesce(this, a);
  }

  public NumberFreeExpression coalesce(final Number a) {
    return new NumberCoalesce(this, BoxUtil.box(a));
  }

  // NullIf

  public NumberFreeExpression nullIf(final NumberExpression a) {
    return new NumberNullIf(this, a);
  }

  public NumberFreeExpression nullIf(final Number a) {
    return new NumberNullIf(this, BoxUtil.box(a));
  }

  // Basic arithmetic

  public NumberFreeExpression plus(final NumberExpression n) {
    return new Plus(this, n);
  }

  public NumberFreeExpression plus(final Number n) {
    return new Plus(this, BoxUtil.box(n));
  }

  public NumberFreeExpression minus(final NumberExpression n) {
    return new Minus(this, n);
  }

  public NumberFreeExpression minus(final Number n) {
    return new Minus(this, BoxUtil.box(n));
  }

  public NumberFreeExpression mult(final NumberExpression n) {
    return new Mult(this, n);
  }

  public NumberFreeExpression mult(final Number n) {
    return new Mult(this, BoxUtil.box(n));
  }

  public NumberFreeExpression div(final NumberExpression n) {
    return new Div(this, n);
  }

  public NumberFreeExpression div(final Number n) {
    return new Div(this, BoxUtil.box(n));
  }

  public NumberFreeExpression remainder(final NumberExpression n) {
    return new Remainder(this, n);
  }

  public NumberFreeExpression remainder(final Number n) {
    return new Remainder(this, BoxUtil.box(n));
  }

  public NumberFreeExpression pow(final NumberExpression exponent) {
    return new Power(this, exponent);
  }

  public NumberFreeExpression pow(final Number exponent) {
    return new Power(this, BoxUtil.box(exponent));
  }

  public NumberFreeExpression log(final NumberExpression base) {
    return new Log(this, base);
  }

  public NumberFreeExpression log(final Number base) {
    return new Log(this, new NumberConstant(base));
  }

  public NumberFreeExpression round() {
    return new Round(this, BoxUtil.box(0));
  }

  public NumberFreeExpression round(final NumberExpression places) {
    return new Round(this, places);
  }

  public NumberFreeExpression round(final Number places) {
    return new Round(this, BoxUtil.box(places));
  }

  public NumberFreeExpression trunc() {
    return new Trunc(this, BoxUtil.box(0));
  }

  public NumberFreeExpression trunc(final NumberExpression places) {
    return new Trunc(this, places);
  }

  public NumberFreeExpression trunc(final Number places) {
    return new Trunc(this, BoxUtil.box(places));
  }

  public NumberFreeExpression neg() {
    return new Neg(this);
  }

  public NumberFreeExpression abs() {
    return new Abs(this);
  }

  public NumberFreeExpression signum() {
    return new Signum(this);
  }

  // Scalar comparisons

  // Equal

  public BooleanFreeExpression eq(final NumberExpression e) {
    return new Equal(this, e);
  }

  public BooleanFreeExpression eq(final Number value) {
    return new Equal(this, BoxUtil.box(value));
  }

  // Not Equal

  public BooleanFreeExpression ne(final NumberExpression e) {
    return new NotEqual(this, e);
  }

  public BooleanFreeExpression ne(final Number value) {
    return new NotEqual(this, BoxUtil.box(value));
  }

  // Greater Than

  public BooleanFreeExpression gt(final NumberExpression e) {
    return new GreaterThan(this, e);
  }

  public BooleanFreeExpression gt(final Number value) {
    return new GreaterThan(this, BoxUtil.box(value));
  }

  // Greater Than or Equal To

  public BooleanFreeExpression ge(final NumberExpression e) {
    return new GreaterThanOrEqualTo(this, e);
  }

  public BooleanFreeExpression ge(final Number value) {
    return new GreaterThanOrEqualTo(this, BoxUtil.box(value));
  }

  // Less Than

  public BooleanFreeExpression lt(final NumberExpression e) {
    return new LessThan(this, e);
  }

  public BooleanFreeExpression lt(final Number value) {
    return new LessThan(this, BoxUtil.box(value));
  }

  // Less Than or Equal To

  public BooleanFreeExpression le(final NumberExpression e) {
    return new LessThanOrEqualTo(this, e);
  }

  public BooleanFreeExpression le(final Number value) {
    return new LessThanOrEqualTo(this, BoxUtil.box(value));
  }

  // Between

  public BooleanFreeExpression between(final NumberExpression from, final NumberExpression to) {
    return new Between(this, from, to);
  }

  public BooleanFreeExpression between(final NumberExpression from, final Number to) {
    return new Between(this, from, BoxUtil.box(to));
  }

  public BooleanFreeExpression between(final Number from, final NumberExpression to) {
    return new Between(this, BoxUtil.box(from), to);
  }

  public BooleanFreeExpression between(final Number from, final Number to) {
    return new Between(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  // Not Between

  public BooleanFreeExpression notBetween(final NumberExpression from, final NumberExpression to) {
    return new NotBetween<NumberExpression>(this, from, to);
  }

  public BooleanFreeExpression notBetween(final NumberExpression from, final Number to) {
    return new NotBetween<NumberExpression>(this, from, BoxUtil.box(to));
  }

  public BooleanFreeExpression notBetween(final Number from, final NumberExpression to) {
    return new NotBetween<NumberExpression>(this, BoxUtil.box(from), to);
  }

  public BooleanFreeExpression notBetween(final Number from, Number to) {
    return new NotBetween<NumberExpression>(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  // In list

  public final BooleanFreeExpression in(final NumberExpression... values) {
    return new InList<NumberExpression>(this, Arrays.asList(values));
  }

  public final BooleanFreeExpression in(final Number... values) {
    return new InList<NumberExpression>(this, Stream.of(values).map(v -> BoxUtil.box(v)).collect(Collectors.toList()));
  }

  public final BooleanFreeExpression notIn(final NumberExpression... values) {
    return new NotInList<NumberExpression>(this, Arrays.asList(values));
  }

  public final BooleanFreeExpression notIn(final Number... values) {
    return new NotInList<NumberExpression>(this,
        Stream.of(values).map(v -> BoxUtil.box(v)).collect(Collectors.toList()));
  }

}
