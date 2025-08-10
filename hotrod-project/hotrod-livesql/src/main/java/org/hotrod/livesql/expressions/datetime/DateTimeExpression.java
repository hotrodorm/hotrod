package org.hotrod.livesql.expressions.datetime;

import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.bool.Between;
import org.hotrod.livesql.expressions.bool.BooleanSyntaxExpression;
import org.hotrod.livesql.expressions.bool.Equal;
import org.hotrod.livesql.expressions.bool.GreaterThan;
import org.hotrod.livesql.expressions.bool.GreaterThanOrEqualTo;
import org.hotrod.livesql.expressions.bool.InList;
import org.hotrod.livesql.expressions.bool.LessThan;
import org.hotrod.livesql.expressions.bool.LessThanOrEqualTo;
import org.hotrod.livesql.expressions.bool.NotBetween;
import org.hotrod.livesql.expressions.bool.NotEqual;
import org.hotrod.livesql.expressions.bool.NotInList;
import org.hotrod.livesql.expressions.datetime.DateTimeFieldExpression.DateTimeField;
import org.hotrod.livesql.expressions.numeric.NumericSyntaxExpression;
import org.hotrod.livesql.queries.subqueries.DateTimeSubqueryExpression;
import org.hotrod.livesql.queries.subqueries.Subquery;
import org.hotrod.livesql.util.BoxUtil;

public abstract class DateTimeExpression extends ComparableExpression {

  protected DateTimeExpression(final int precedence) {
    super(precedence);
  }

  @Override
  protected Expression asSubqueryExpression(final Subquery subquery, final String alias) {
    return new DateTimeSubqueryExpression(subquery, alias, this);
  }

  // Coalesce

  public DateTimeSyntaxExpression coalesce(final DateTimeExpression a) {
    return new DateTimeCoalesce(this, a);
  }

  public DateTimeSyntaxExpression coalesce(final Date a) {
    return new DateTimeCoalesce(this, new DateTimeConstant(a));
  }

  public DateTimeSyntaxExpression coalesce(final Temporal a) {
    return new DateTimeCoalesce(this, new DateTimeConstant(a));
  }

  // NullIf

  public DateTimeSyntaxExpression nullIf(final DateTimeExpression a) {
    return new DateTimeNullIf(this, a);
  }

  public DateTimeSyntaxExpression nullIf(final Date a) {
    return new DateTimeNullIf(this, new DateTimeConstant(a));
  }

  public DateTimeSyntaxExpression nullIf(final Temporal a) {
    return new DateTimeNullIf(this, new DateTimeConstant(a));
  }

  // DateTime Functions

  public DateTimeSyntaxExpression date() {
    return new org.hotrod.livesql.expressions.datetime.Date(this);
  }

  public DateTimeSyntaxExpression time() {
    return new org.hotrod.livesql.expressions.datetime.Time(this);
  }

  public NumericSyntaxExpression extract(final DateTimeField field) {
    return new Extract(this, new DateTimeFieldExpression(field));
  }

  // Scalar comparisons

  // Equal

  public BooleanSyntaxExpression eq(final DateTimeExpression e) {
    return new Equal(this, e);
  }

  public BooleanSyntaxExpression eq(final Date value) {
    return new Equal(this, BoxUtil.box(value));
  }

  public BooleanSyntaxExpression eq(final Temporal value) {
    return new Equal(this, BoxUtil.box(value));
  }

  // Not Equal

  public BooleanSyntaxExpression ne(final DateTimeExpression e) {
    return new NotEqual(this, e);
  }

  public BooleanSyntaxExpression ne(final Date value) {
    return new NotEqual(this, BoxUtil.box(value));
  }

  public BooleanSyntaxExpression ne(final Temporal value) {
    return new NotEqual(this, BoxUtil.box(value));
  }

  // Greater Than

  public BooleanSyntaxExpression gt(final DateTimeExpression e) {
    return new GreaterThan(this, e);
  }

  public BooleanSyntaxExpression gt(final Date value) {
    return new GreaterThan(this, BoxUtil.box(value));
  }

  public BooleanSyntaxExpression gt(final Temporal value) {
    return new GreaterThan(this, BoxUtil.box(value));
  }

  // Greater Than or Equal To

  public BooleanSyntaxExpression ge(final DateTimeExpression e) {
    return new GreaterThanOrEqualTo(this, e);
  }

  public BooleanSyntaxExpression ge(final Date value) {
    return new GreaterThanOrEqualTo(this, BoxUtil.box(value));
  }

  public BooleanSyntaxExpression ge(final Temporal value) {
    return new GreaterThanOrEqualTo(this, BoxUtil.box(value));
  }

  // Less Than

  public BooleanSyntaxExpression lt(final DateTimeExpression e) {
    return new LessThan(this, e);
  }

  public BooleanSyntaxExpression lt(final Date value) {
    return new LessThan(this, BoxUtil.box(value));
  }

  public BooleanSyntaxExpression lt(final Temporal value) {
    return new LessThan(this, BoxUtil.box(value));
  }

  // Less Than or Equal To

  public BooleanSyntaxExpression le(final DateTimeExpression e) {
    return new LessThanOrEqualTo(this, e);
  }

  public BooleanSyntaxExpression le(final Date value) {
    return new LessThanOrEqualTo(this, BoxUtil.box(value));
  }

  public BooleanSyntaxExpression le(final Temporal value) {
    return new LessThanOrEqualTo(this, BoxUtil.box(value));
  }

  // Between

  public BooleanSyntaxExpression between(final DateTimeExpression from, final DateTimeExpression to) {
    return new Between(this, from, to);
  }

  public BooleanSyntaxExpression between(final DateTimeExpression from, final Date to) {
    return new Between(this, from, BoxUtil.box(to));
  }

  public BooleanSyntaxExpression between(final DateTimeExpression from, final Temporal to) {
    return new Between(this, from, BoxUtil.box(to));
  }

  public BooleanSyntaxExpression between(final Date from, final DateTimeExpression to) {
    return new Between(this, BoxUtil.box(from), to);
  }

  public BooleanSyntaxExpression between(final Temporal from, final DateTimeExpression to) {
    return new Between(this, BoxUtil.box(from), to);
  }

  public BooleanSyntaxExpression between(final Date from, final Date to) {
    return new Between(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  public BooleanSyntaxExpression between(final Date from, final Temporal to) {
    return new Between(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  public BooleanSyntaxExpression between(final Temporal from, final Date to) {
    return new Between(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  public BooleanSyntaxExpression between(final Temporal from, final Temporal to) {
    return new Between(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  // Not Between

  public BooleanSyntaxExpression notBetween(final DateTimeExpression from, final DateTimeExpression to) {
    return new NotBetween<DateTimeExpression>(this, from, to);
  }

  public BooleanSyntaxExpression notBetween(final DateTimeExpression from, final Date to) {
    return new NotBetween<DateTimeExpression>(this, from, BoxUtil.box(to));
  }

  public BooleanSyntaxExpression notBetween(final DateTimeExpression from, final Temporal to) {
    return new NotBetween<DateTimeExpression>(this, from, BoxUtil.box(to));
  }

  public BooleanSyntaxExpression notBetween(final Date from, final DateTimeExpression to) {
    return new NotBetween<DateTimeExpression>(this, BoxUtil.box(from), to);
  }

  public BooleanSyntaxExpression notBetween(final Temporal from, final DateTimeExpression to) {
    return new NotBetween<DateTimeExpression>(this, BoxUtil.box(from), to);
  }

  public BooleanSyntaxExpression notBetween(final Date from, Date to) {
    return new NotBetween<DateTimeExpression>(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  public BooleanSyntaxExpression notBetween(final Date from, Temporal to) {
    return new NotBetween<DateTimeExpression>(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  public BooleanSyntaxExpression notBetween(final Temporal from, Date to) {
    return new NotBetween<DateTimeExpression>(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  public BooleanSyntaxExpression notBetween(final Temporal from, Temporal to) {
    return new NotBetween<DateTimeExpression>(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  // In list

  public final BooleanSyntaxExpression in(final DateTimeExpression... values) {
    return new InList<DateTimeExpression>(this, Arrays.asList(values));
  }

  public final BooleanSyntaxExpression in(final Date... values) {
    return new InList<DateTimeExpression>(this,
        Stream.of(values).map(v -> BoxUtil.box(v)).collect(Collectors.toList()));
  }

  public final BooleanSyntaxExpression in(final Temporal... values) {
    return new InList<DateTimeExpression>(this,
        Stream.of(values).map(v -> BoxUtil.box(v)).collect(Collectors.toList()));
  }

  public final BooleanSyntaxExpression notIn(final DateTimeExpression... values) {
    return new NotInList<DateTimeExpression>(this, Arrays.asList(values));
  }

  public final BooleanSyntaxExpression notIn(final Date... values) {
    return new NotInList<DateTimeExpression>(this,
        Stream.of(values).map(v -> BoxUtil.box(v)).collect(Collectors.toList()));
  }

  public final BooleanSyntaxExpression notIn(final Temporal... values) {
    return new NotInList<DateTimeExpression>(this,
        Stream.of(values).map(v -> BoxUtil.box(v)).collect(Collectors.toList()));
  }

}
