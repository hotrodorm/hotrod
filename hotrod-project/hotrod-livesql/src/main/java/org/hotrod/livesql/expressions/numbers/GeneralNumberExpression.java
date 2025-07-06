package org.hotrod.livesql.expressions.numbers;

import java.util.Arrays;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.predicates.Between;
import org.hotrod.livesql.expressions.predicates.Equal;
import org.hotrod.livesql.expressions.predicates.GreaterThan;
import org.hotrod.livesql.expressions.predicates.GreaterThanOrEqualTo;
import org.hotrod.livesql.expressions.predicates.InList;
import org.hotrod.livesql.expressions.predicates.LessThan;
import org.hotrod.livesql.expressions.predicates.LessThanOrEqualTo;
import org.hotrod.livesql.expressions.predicates.NotBetween;
import org.hotrod.livesql.expressions.predicates.NotEqual;
import org.hotrod.livesql.expressions.predicates.NotInList;
import org.hotrod.livesql.expressions.predicates.Predicate;
import org.hotrod.livesql.queries.select.SShield;
import org.hotrod.livesql.queries.subqueries.Subquery;
import org.hotrod.livesql.queries.subqueries.SubqueryNumberRefColumn;
import org.hotrod.livesql.util.BoxUtil;

/*
 
 GenericNumberExpression
  - EntityNumberExpression
  - NumberExpression

 GeneralNumberExpression
  - EntityNumberExpression
  - NumberExpression

Common
Standard
Simple
Classic
Regular
Natural
Universal
Conventional
Typical
Formal
General
Plain
Straight

 */
public abstract class GeneralNumberExpression extends ComparableExpression {

  private static final Logger log = Logger.getLogger(GeneralNumberExpression.class.getName());

  protected GeneralNumberExpression(final int precedence) {
    super(precedence);
  }

  @Override
  protected Expression asSubqueryExpression(final Subquery subquery, final String alias) {
    log.info("@@@  asSubqueryExpression " + SShield.getName(subquery) + "." + alias);
    return new SubqueryNumberRefColumn(subquery, alias);
  }

  // Coalesce

  public NumberExpression coalesce(final GeneralNumberExpression a) {
    return new NumberCoalesce(this, a);
  }

  public NumberExpression coalesce(final Number a) {
    return new NumberCoalesce(this, BoxUtil.box(a));
  }

  // NullIf

  public NumberExpression nullIf(final GeneralNumberExpression a) {
    return new NumberNullIf(this, a);
  }

  public NumberExpression nullIf(final Number a) {
    return new NumberNullIf(this, BoxUtil.box(a));
  }

  // Basic arithmetic

  public NumberExpression plus(final GeneralNumberExpression n) {
    return new Plus(this, n);
  }

  public NumberExpression plus(final Number n) {
    return new Plus(this, BoxUtil.box(n));
  }

  public NumberExpression minus(final GeneralNumberExpression n) {
    return new Minus(this, n);
  }

  public NumberExpression minus(final Number n) {
    return new Minus(this, BoxUtil.box(n));
  }

  public NumberExpression mult(final GeneralNumberExpression n) {
    return new Mult(this, n);
  }

  public NumberExpression mult(final Number n) {
    return new Mult(this, BoxUtil.box(n));
  }

  public NumberExpression div(final GeneralNumberExpression n) {
    return new Div(this, n);
  }

  public NumberExpression div(final Number n) {
    return new Div(this, BoxUtil.box(n));
  }

  public NumberExpression remainder(final GeneralNumberExpression n) {
    return new Remainder(this, n);
  }

  public NumberExpression remainder(final Number n) {
    return new Remainder(this, BoxUtil.box(n));
  }

  public NumberExpression pow(final GeneralNumberExpression exponent) {
    return new Power(this, exponent);
  }

  public NumberExpression pow(final Number exponent) {
    return new Power(this, BoxUtil.box(exponent));
  }

  public NumberExpression log(final GeneralNumberExpression base) {
    return new Log(this, base);
  }

  public NumberExpression log(final Number base) {
    return new Log(this, new NumberConstant(base));
  }

  public NumberExpression round() {
    return new Round(this, BoxUtil.box(0));
  }

  public NumberExpression round(final GeneralNumberExpression places) {
    return new Round(this, places);
  }

  public NumberExpression round(final Number places) {
    return new Round(this, BoxUtil.box(places));
  }

  public NumberExpression trunc() {
    return new Trunc(this, BoxUtil.box(0));
  }

  public NumberExpression trunc(final GeneralNumberExpression places) {
    return new Trunc(this, places);
  }

  public NumberExpression trunc(final Number places) {
    return new Trunc(this, BoxUtil.box(places));
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

  // Scalar comparisons

  // Equal

  public Predicate eq(final GeneralNumberExpression e) {
    return new Equal(this, e);
  }

  public Predicate eq(final Number value) {
    return new Equal(this, BoxUtil.box(value));
  }

  // Not Equal

  public Predicate ne(final GeneralNumberExpression e) {
    return new NotEqual(this, e);
  }

  public Predicate ne(final Number value) {
    return new NotEqual(this, BoxUtil.box(value));
  }

  // Greater Than

  public Predicate gt(final GeneralNumberExpression e) {
    return new GreaterThan(this, e);
  }

  public Predicate gt(final Number value) {
    return new GreaterThan(this, BoxUtil.box(value));
  }

  // Greater Than or Equal To

  public Predicate ge(final GeneralNumberExpression e) {
    return new GreaterThanOrEqualTo(this, e);
  }

  public Predicate ge(final Number value) {
    return new GreaterThanOrEqualTo(this, BoxUtil.box(value));
  }

  // Less Than

  public Predicate lt(final GeneralNumberExpression e) {
    return new LessThan(this, e);
  }

  public Predicate lt(final Number value) {
    return new LessThan(this, BoxUtil.box(value));
  }

  // Less Than or Equal To

  public Predicate le(final GeneralNumberExpression e) {
    return new LessThanOrEqualTo(this, e);
  }

  public Predicate le(final Number value) {
    return new LessThanOrEqualTo(this, BoxUtil.box(value));
  }

  // Between

  public Predicate between(final GeneralNumberExpression from, final GeneralNumberExpression to) {
    return new Between(this, from, to);
  }

  public Predicate between(final GeneralNumberExpression from, final Number to) {
    return new Between(this, from, BoxUtil.box(to));
  }

  public Predicate between(final Number from, final GeneralNumberExpression to) {
    return new Between(this, BoxUtil.box(from), to);
  }

  public Predicate between(final Number from, final Number to) {
    return new Between(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  // Not Between

  public Predicate notBetween(final GeneralNumberExpression from, final GeneralNumberExpression to) {
    return new NotBetween<GeneralNumberExpression>(this, from, to);
  }

  public Predicate notBetween(final GeneralNumberExpression from, final Number to) {
    return new NotBetween<GeneralNumberExpression>(this, from, BoxUtil.box(to));
  }

  public Predicate notBetween(final Number from, final GeneralNumberExpression to) {
    return new NotBetween<GeneralNumberExpression>(this, BoxUtil.box(from), to);
  }

  public Predicate notBetween(final Number from, Number to) {
    return new NotBetween<GeneralNumberExpression>(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  // In list

  public final Predicate in(final GeneralNumberExpression... values) {
    return new InList<GeneralNumberExpression>(this, Arrays.asList(values));
  }

  public final Predicate in(final Number... values) {
    return new InList<GeneralNumberExpression>(this,
        Stream.of(values).map(v -> BoxUtil.box(v)).collect(Collectors.toList()));
  }

  public final Predicate notIn(final GeneralNumberExpression... values) {
    return new NotInList<GeneralNumberExpression>(this, Arrays.asList(values));
  }

  public final Predicate notIn(final Number... values) {
    return new NotInList<GeneralNumberExpression>(this,
        Stream.of(values).map(v -> BoxUtil.box(v)).collect(Collectors.toList()));
  }

}
