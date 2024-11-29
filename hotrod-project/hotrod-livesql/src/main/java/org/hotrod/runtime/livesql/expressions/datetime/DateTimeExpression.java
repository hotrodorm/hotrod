package org.hotrod.runtime.livesql.expressions.datetime;

import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hotrod.runtime.livesql.expressions.ComparableExpression;
import org.hotrod.runtime.livesql.expressions.datetime.DateTimeFieldExpression.DateTimeField;
import org.hotrod.runtime.livesql.expressions.numbers.NumberFreeExpression;
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

public abstract class DateTimeExpression extends ComparableExpression {

  protected DateTimeExpression(final int precedence) {
    super(precedence);
  }

  // Coalesce

  public DateTimeFreeExpression coalesce(final DateTimeExpression a) {
    return new DateTimeCoalesce(this, a);
  }

  public DateTimeFreeExpression coalesce(final Date a) {
    return new DateTimeCoalesce(this, new DateTimeConstant(a));
  }

  // NullIf

  public DateTimeFreeExpression nullIf(final DateTimeExpression a) {
    return new DateTimeNullIf(this, a);
  }

  public DateTimeFreeExpression nullIf(final Date a) {
    return new DateTimeNullIf(this, new DateTimeConstant(a));
  }

  // DateTime Functions

  public DateTimeFreeExpression date() {
    return new org.hotrod.runtime.livesql.expressions.datetime.Date(this);
  }

  public DateTimeExpression time() {
    return new org.hotrod.runtime.livesql.expressions.datetime.Time(this);
  }

  public NumberFreeExpression extract(final DateTimeField field) {
    return new Extract(this, new DateTimeFieldExpression(field));
  }

  // Scalar comparisons

  // Equal

  public BooleanFreeExpression eq(final DateTimeExpression e) {
    return new Equal(this, e);
  }

  public BooleanFreeExpression eq(final Object value) {
    return new Equal(this, BoxUtil.box(value));
  }

  // Not Equal

  public BooleanFreeExpression ne(final DateTimeExpression e) {
    return new NotEqual(this, e);
  }

  public BooleanFreeExpression ne(final Object value) {
    return new NotEqual(this, BoxUtil.box(value));
  }

  // Greater Than

  public BooleanFreeExpression gt(final DateTimeExpression e) {
    return new GreaterThan(this, e);
  }

  public BooleanFreeExpression gt(final Object value) {
    return new GreaterThan(this, BoxUtil.box(value));
  }

  // Greater Than or Equal To

  public BooleanFreeExpression ge(final DateTimeExpression e) {
    return new GreaterThanOrEqualTo(this, e);
  }

  public BooleanFreeExpression ge(final Object value) {
    return new GreaterThanOrEqualTo(this, BoxUtil.box(value));
  }

  // Less Than

  public BooleanFreeExpression lt(final DateTimeExpression e) {
    return new LessThan(this, e);
  }

  public BooleanFreeExpression lt(final Object value) {
    return new LessThan(this, BoxUtil.box(value));
  }

  // Less Than or Equal To

  public BooleanFreeExpression le(final DateTimeExpression e) {
    return new LessThanOrEqualTo(this, e);
  }

  public BooleanFreeExpression le(final Object value) {
    return new LessThanOrEqualTo(this, BoxUtil.box(value));
  }

  // Between

  public BooleanFreeExpression between(final DateTimeExpression from, final DateTimeExpression to) {
    return new Between(this, from, to);
  }

  public BooleanFreeExpression between(final DateTimeExpression from, final Object to) {
    return new Between(this, from, BoxUtil.box(to));
  }

  public BooleanFreeExpression between(final Object from, final DateTimeExpression to) {
    return new Between(this, BoxUtil.box(from), to);
  }

  public BooleanFreeExpression between(final Object from, final Object to) {
    return new Between(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  // Not Between

  public BooleanFreeExpression notBetween(final DateTimeExpression from, final DateTimeExpression to) {
    return new NotBetween<DateTimeExpression>(this, from, to);
  }

  public BooleanFreeExpression notBetween(final DateTimeExpression from, final Date to) {
    return new NotBetween<DateTimeExpression>(this, from, BoxUtil.box(to));
  }

  public BooleanFreeExpression notBetween(final Date from, final DateTimeExpression to) {
    return new NotBetween<DateTimeExpression>(this, BoxUtil.box(from), to);
  }

  public BooleanFreeExpression notBetween(final Date from, Date to) {
    return new NotBetween<DateTimeExpression>(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  // In list

  public final BooleanFreeExpression in(final DateTimeExpression... values) {
    return new InList<DateTimeExpression>(this, Arrays.asList(values));
  }

  public final BooleanFreeExpression in(final Date... values) {
    return new InList<DateTimeExpression>(this,
        Stream.of(values).map(v -> BoxUtil.box(v)).collect(Collectors.toList()));
  }

  public final BooleanFreeExpression notIn(final DateTimeExpression... values) {
    return new NotInList<DateTimeExpression>(this, Arrays.asList(values));
  }

  public final BooleanFreeExpression notIn(final Date... values) {
    return new NotInList<DateTimeExpression>(this,
        Stream.of(values).map(v -> BoxUtil.box(v)).collect(Collectors.toList()));
  }

  // TODO: END -- implement in subclasses

}
