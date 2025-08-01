package org.hotrod.livesql.expressions.numeric;

import java.util.Arrays;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.bool.Between;
import org.hotrod.livesql.expressions.bool.Equal;
import org.hotrod.livesql.expressions.bool.GreaterThan;
import org.hotrod.livesql.expressions.bool.GreaterThanOrEqualTo;
import org.hotrod.livesql.expressions.bool.InList;
import org.hotrod.livesql.expressions.bool.LessThan;
import org.hotrod.livesql.expressions.bool.LessThanOrEqualTo;
import org.hotrod.livesql.expressions.bool.NotBetween;
import org.hotrod.livesql.expressions.bool.NotEqual;
import org.hotrod.livesql.expressions.bool.NotInList;
import org.hotrod.livesql.expressions.bool.Predicate;
import org.hotrod.livesql.queries.select.SShield;
import org.hotrod.livesql.queries.subqueries.Subquery;
import org.hotrod.livesql.queries.subqueries.SubqueryNumericRefColumn;
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
public abstract class GeneralNumericExpression extends ComparableExpression {

  private static final Logger log = Logger.getLogger(GeneralNumericExpression.class.getName());

  protected GeneralNumericExpression(final int precedence) {
    super(precedence);
  }

  @Override
  protected Expression asSubqueryExpression(final Subquery subquery, final String alias) {
//    log.info("@@@  asSubqueryExpression " + SShield.getName(subquery) + "." + alias);
    return new SubqueryNumericRefColumn(subquery, alias, this);
  }

  // Coalesce

  public NumericExpression coalesce(final GeneralNumericExpression a) {
    return new NumericCoalesce(this, a);
  }

  public NumericExpression coalesce(final Number a) {
    return new NumericCoalesce(this, BoxUtil.box(a));
  }

  // NullIf

  public NumericExpression nullIf(final GeneralNumericExpression a) {
    return new NumericNullIf(this, a);
  }

  public NumericExpression nullIf(final Number a) {
    return new NumericNullIf(this, BoxUtil.box(a));
  }

  // Basic arithmetic

  public NumericExpression plus(final GeneralNumericExpression n) {
    return new Plus(this, n);
  }

  public NumericExpression plus(final Number n) {
    return new Plus(this, BoxUtil.box(n));
  }

  public NumericExpression minus(final GeneralNumericExpression n) {
    return new Minus(this, n);
  }

  public NumericExpression minus(final Number n) {
    return new Minus(this, BoxUtil.box(n));
  }

  public NumericExpression mult(final GeneralNumericExpression n) {
    return new Mult(this, n);
  }

  public NumericExpression mult(final Number n) {
    return new Mult(this, BoxUtil.box(n));
  }

  public NumericExpression div(final GeneralNumericExpression n) {
    return new Div(this, n);
  }

  public NumericExpression div(final Number n) {
    return new Div(this, BoxUtil.box(n));
  }

  public NumericExpression remainder(final GeneralNumericExpression n) {
    return new Remainder(this, n);
  }

  public NumericExpression remainder(final Number n) {
    return new Remainder(this, BoxUtil.box(n));
  }

  public NumericExpression pow(final GeneralNumericExpression exponent) {
    return new Power(this, exponent);
  }

  public NumericExpression pow(final Number exponent) {
    return new Power(this, BoxUtil.box(exponent));
  }

  public NumericExpression log(final GeneralNumericExpression base) {
    return new Log(this, base);
  }

  public NumericExpression log(final Number base) {
    return new Log(this, new NumericConstant(base));
  }

  public NumericExpression round() {
    return new Round(this, BoxUtil.box(0));
  }

  public NumericExpression round(final GeneralNumericExpression places) {
    return new Round(this, places);
  }

  public NumericExpression round(final Number places) {
    return new Round(this, BoxUtil.box(places));
  }

  public NumericExpression trunc() {
    return new Trunc(this, BoxUtil.box(0));
  }

  public NumericExpression trunc(final GeneralNumericExpression places) {
    return new Trunc(this, places);
  }

  public NumericExpression trunc(final Number places) {
    return new Trunc(this, BoxUtil.box(places));
  }

  public NumericExpression neg() {
    return new Neg(this);
  }

  public NumericExpression abs() {
    return new Abs(this);
  }

  public NumericExpression signum() {
    return new Signum(this);
  }

  // Scalar comparisons

  // Equal

  public Predicate eq(final GeneralNumericExpression e) {
    return new Equal(this, e);
  }

  public Predicate eq(final Number value) {
    return new Equal(this, BoxUtil.box(value));
  }

  // Not Equal

  public Predicate ne(final GeneralNumericExpression e) {
    return new NotEqual(this, e);
  }

  public Predicate ne(final Number value) {
    return new NotEqual(this, BoxUtil.box(value));
  }

  // Greater Than

  public Predicate gt(final GeneralNumericExpression e) {
    return new GreaterThan(this, e);
  }

  public Predicate gt(final Number value) {
    return new GreaterThan(this, BoxUtil.box(value));
  }

  // Greater Than or Equal To

  public Predicate ge(final GeneralNumericExpression e) {
    return new GreaterThanOrEqualTo(this, e);
  }

  public Predicate ge(final Number value) {
    return new GreaterThanOrEqualTo(this, BoxUtil.box(value));
  }

  // Less Than

  public Predicate lt(final GeneralNumericExpression e) {
    return new LessThan(this, e);
  }

  public Predicate lt(final Number value) {
    return new LessThan(this, BoxUtil.box(value));
  }

  // Less Than or Equal To

  public Predicate le(final GeneralNumericExpression e) {
    return new LessThanOrEqualTo(this, e);
  }

  public Predicate le(final Number value) {
    return new LessThanOrEqualTo(this, BoxUtil.box(value));
  }

  // Between

  public Predicate between(final GeneralNumericExpression from, final GeneralNumericExpression to) {
    return new Between(this, from, to);
  }

  public Predicate between(final GeneralNumericExpression from, final Number to) {
    return new Between(this, from, BoxUtil.box(to));
  }

  public Predicate between(final Number from, final GeneralNumericExpression to) {
    return new Between(this, BoxUtil.box(from), to);
  }

  public Predicate between(final Number from, final Number to) {
    return new Between(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  // Not Between

  public Predicate notBetween(final GeneralNumericExpression from, final GeneralNumericExpression to) {
    return new NotBetween<GeneralNumericExpression>(this, from, to);
  }

  public Predicate notBetween(final GeneralNumericExpression from, final Number to) {
    return new NotBetween<GeneralNumericExpression>(this, from, BoxUtil.box(to));
  }

  public Predicate notBetween(final Number from, final GeneralNumericExpression to) {
    return new NotBetween<GeneralNumericExpression>(this, BoxUtil.box(from), to);
  }

  public Predicate notBetween(final Number from, Number to) {
    return new NotBetween<GeneralNumericExpression>(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  // In list

  public final Predicate in(final GeneralNumericExpression... values) {
    return new InList<GeneralNumericExpression>(this, Arrays.asList(values));
  }

  public final Predicate in(final Number... values) {
    return new InList<GeneralNumericExpression>(this,
        Stream.of(values).map(v -> BoxUtil.box(v)).collect(Collectors.toList()));
  }

  public final Predicate notIn(final GeneralNumericExpression... values) {
    return new NotInList<GeneralNumericExpression>(this, Arrays.asList(values));
  }

  public final Predicate notIn(final Number... values) {
    return new NotInList<GeneralNumericExpression>(this,
        Stream.of(values).map(v -> BoxUtil.box(v)).collect(Collectors.toList()));
  }

}
