package org.hotrod.runtime.livesql.expressions.numbers;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.predicates.Between;
import org.hotrod.runtime.livesql.expressions.predicates.Equal;
import org.hotrod.runtime.livesql.expressions.predicates.GreaterThan;
import org.hotrod.runtime.livesql.expressions.predicates.GreaterThanOrEqualTo;
import org.hotrod.runtime.livesql.expressions.predicates.InList;
import org.hotrod.runtime.livesql.expressions.predicates.LessThan;
import org.hotrod.runtime.livesql.expressions.predicates.LessThanOrEqualTo;
import org.hotrod.runtime.livesql.expressions.predicates.NotBetween;
import org.hotrod.runtime.livesql.expressions.predicates.NotEqual;
import org.hotrod.runtime.livesql.expressions.predicates.NotInList;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.util.BoxUtil;

public abstract class NumberExpression extends Expression {

  protected NumberExpression(final int precedence) {
    super(precedence);
  }

  // Coalesce

  public NumberExpression coalesce(final NumberExpression a) {
    return new NumberCoalesce(this, a);
    // NumberExpression[] values = AUtil.concat(this, a);
    // return new NumberCoalesce(values);
  }

  public NumberExpression coalesce(final Number a) {
    return new NumberCoalesce(this, new NumberConstant(a));
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

  // TODO: implement in subclasses

  // Scalar comparisons

  // Equal

  public Predicate eq(final NumberExpression e) {
    return new Equal(this, e);
  }

  public Predicate eq(final Number value) {
    return new Equal(this, BoxUtil.box(value));
  }

  // Not Equal

  public Predicate ne(final NumberExpression e) {
    return new NotEqual(this, e);
  }

  public Predicate ne(final Number value) {
    return new NotEqual(this, BoxUtil.box(value));
  }

  // Greater Than

  public Predicate gt(final NumberExpression e) {
    return new GreaterThan(this, e);
  }

  public Predicate gt(final Number value) {
    return new GreaterThan(this, BoxUtil.box(value));
  }

  // Greater Than or Equal To

  public Predicate ge(final NumberExpression e) {
    return new GreaterThanOrEqualTo(this, e);
  }

  public Predicate ge(final Number value) {
    return new GreaterThanOrEqualTo(this, BoxUtil.box(value));
  }

  // Less Than

  public Predicate lt(final NumberExpression e) {
    return new LessThan(this, e);
  }

  public Predicate lt(final Number value) {
    return new LessThan(this, BoxUtil.box(value));
  }

  // Less Than or Equal To

  public Predicate le(final NumberExpression e) {
    return new LessThanOrEqualTo(this, e);
  }

  public Predicate le(final Number value) {
    return new LessThanOrEqualTo(this, BoxUtil.box(value));
  }

  // Between

  public Predicate between(final NumberExpression from, final NumberExpression to) {
    return new Between(this, from, to);
  }

  public Predicate between(final NumberExpression from, final Number to) {
    return new Between(this, from, BoxUtil.box(to));
  }

  public Predicate between(final Number from, final NumberExpression to) {
    return new Between(this, BoxUtil.box(from), to);
  }

  public Predicate between(final Number from, final Number to) {
    return new Between(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  // Not Between

  public Predicate notBetween(final NumberExpression from, final NumberExpression to) {
    return new NotBetween<NumberExpression>(this, from, to);
  }

  public Predicate notBetween(final NumberExpression from, final Number to) {
    return new NotBetween<NumberExpression>(this, from, BoxUtil.box(to));
  }

  public Predicate notBetween(final Number from, final NumberExpression to) {
    return new NotBetween<NumberExpression>(this, BoxUtil.box(from), to);
  }

  public Predicate notBetween(final Number from, Number to) {
    return new NotBetween<NumberExpression>(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  // In list

  public final Predicate in(final NumberExpression... values) {
    return new InList<NumberExpression>(this, Arrays.asList(values));
  }

  public final Predicate in(final Number... values) {
    return new InList<NumberExpression>(this, Stream.of(values).map(v -> BoxUtil.box(v)).collect(Collectors.toList()));
  }

  public final Predicate notIn(final NumberExpression... values) {
    return new NotInList<NumberExpression>(this, Arrays.asList(values));
  }

  public final Predicate notIn(final Number... values) {
    return new NotInList<NumberExpression>(this,
        Stream.of(values).map(v -> BoxUtil.box(v)).collect(Collectors.toList()));
  }

  // TODO: END -- implement in subclasses

}
