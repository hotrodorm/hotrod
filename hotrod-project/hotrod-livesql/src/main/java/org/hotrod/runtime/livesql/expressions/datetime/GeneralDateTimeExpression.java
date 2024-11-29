package org.hotrod.runtime.livesql.expressions.datetime;

import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hotrod.runtime.livesql.expressions.ComparableExpression;
import org.hotrod.runtime.livesql.expressions.datetime.DateTimeFieldExpression.DateTimeField;
import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;
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

public abstract class GeneralDateTimeExpression extends ComparableExpression {

  protected GeneralDateTimeExpression(final int precedence) {
    super(precedence);
  }

  // Coalesce

  public DateTimeExpression coalesce(final GeneralDateTimeExpression a) {
    return new DateTimeCoalesce(this, a);
  }

  public DateTimeExpression coalesce(final Date a) {
    return new DateTimeCoalesce(this, new DateTimeConstant(a));
  }

  // NullIf

  public DateTimeExpression nullIf(final GeneralDateTimeExpression a) {
    return new DateTimeNullIf(this, a);
  }

  public DateTimeExpression nullIf(final Date a) {
    return new DateTimeNullIf(this, new DateTimeConstant(a));
  }

  // DateTime Functions

  public DateTimeExpression date() {
    return new org.hotrod.runtime.livesql.expressions.datetime.Date(this);
  }

  public DateTimeExpression time() {
    return new org.hotrod.runtime.livesql.expressions.datetime.Time(this);
  }

  public NumberExpression extract(final DateTimeField field) {
    return new Extract(this, new DateTimeFieldExpression(field));
  }

  // Scalar comparisons

  // Equal

  public Predicate eq(final GeneralDateTimeExpression e) {
    return new Equal(this, e);
  }

  public Predicate eq(final Object value) {
    return new Equal(this, BoxUtil.box(value));
  }

  // Not Equal

  public Predicate ne(final GeneralDateTimeExpression e) {
    return new NotEqual(this, e);
  }

  public Predicate ne(final Object value) {
    return new NotEqual(this, BoxUtil.box(value));
  }

  // Greater Than

  public Predicate gt(final GeneralDateTimeExpression e) {
    return new GreaterThan(this, e);
  }

  public Predicate gt(final Object value) {
    return new GreaterThan(this, BoxUtil.box(value));
  }

  // Greater Than or Equal To

  public Predicate ge(final GeneralDateTimeExpression e) {
    return new GreaterThanOrEqualTo(this, e);
  }

  public Predicate ge(final Object value) {
    return new GreaterThanOrEqualTo(this, BoxUtil.box(value));
  }

  // Less Than

  public Predicate lt(final GeneralDateTimeExpression e) {
    return new LessThan(this, e);
  }

  public Predicate lt(final Object value) {
    return new LessThan(this, BoxUtil.box(value));
  }

  // Less Than or Equal To

  public Predicate le(final GeneralDateTimeExpression e) {
    return new LessThanOrEqualTo(this, e);
  }

  public Predicate le(final Object value) {
    return new LessThanOrEqualTo(this, BoxUtil.box(value));
  }

  // Between

  public Predicate between(final GeneralDateTimeExpression from, final GeneralDateTimeExpression to) {
    return new Between(this, from, to);
  }

  public Predicate between(final GeneralDateTimeExpression from, final Object to) {
    return new Between(this, from, BoxUtil.box(to));
  }

  public Predicate between(final Object from, final GeneralDateTimeExpression to) {
    return new Between(this, BoxUtil.box(from), to);
  }

  public Predicate between(final Object from, final Object to) {
    return new Between(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  // Not Between

  public Predicate notBetween(final GeneralDateTimeExpression from, final GeneralDateTimeExpression to) {
    return new NotBetween<GeneralDateTimeExpression>(this, from, to);
  }

  public Predicate notBetween(final GeneralDateTimeExpression from, final Date to) {
    return new NotBetween<GeneralDateTimeExpression>(this, from, BoxUtil.box(to));
  }

  public Predicate notBetween(final Date from, final GeneralDateTimeExpression to) {
    return new NotBetween<GeneralDateTimeExpression>(this, BoxUtil.box(from), to);
  }

  public Predicate notBetween(final Date from, Date to) {
    return new NotBetween<GeneralDateTimeExpression>(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  // In list

  public final Predicate in(final GeneralDateTimeExpression... values) {
    return new InList<GeneralDateTimeExpression>(this, Arrays.asList(values));
  }

  public final Predicate in(final Date... values) {
    return new InList<GeneralDateTimeExpression>(this,
        Stream.of(values).map(v -> BoxUtil.box(v)).collect(Collectors.toList()));
  }

  public final Predicate notIn(final GeneralDateTimeExpression... values) {
    return new NotInList<GeneralDateTimeExpression>(this, Arrays.asList(values));
  }

  public final Predicate notIn(final Date... values) {
    return new NotInList<GeneralDateTimeExpression>(this,
        Stream.of(values).map(v -> BoxUtil.box(v)).collect(Collectors.toList()));
  }

  // TODO: END -- implement in subclasses

}
