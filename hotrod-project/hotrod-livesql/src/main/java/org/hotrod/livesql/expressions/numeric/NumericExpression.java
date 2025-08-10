package org.hotrod.livesql.expressions.numeric;

import java.util.Arrays;
import java.util.logging.Logger;
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
import org.hotrod.livesql.queries.subqueries.NumericSubqueryExpression;
import org.hotrod.livesql.queries.subqueries.Subquery;
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
public abstract class NumericExpression extends ComparableExpression {

  private static final Logger log = Logger.getLogger(NumericExpression.class.getName());

  protected NumericExpression(final int precedence) {
    super(precedence);
  }

  @Override
  protected Expression asSubqueryExpression(final Subquery subquery, final String alias) {
//    log.info("@@@  asSubqueryExpression " + SShield.getName(subquery) + "." + alias);
    return new NumericSubqueryExpression(subquery, alias, this);
  }

  // Coalesce

  public NumericSyntaxExpression coalesce(final NumericExpression a) {
    return new NumericCoalesce(this, a);
  }

  public NumericSyntaxExpression coalesce(final Number a) {
    return new NumericCoalesce(this, BoxUtil.box(a));
  }

  // NullIf

  public NumericSyntaxExpression nullIf(final NumericExpression a) {
    return new NumericNullIf(this, a);
  }

  public NumericSyntaxExpression nullIf(final Number a) {
    return new NumericNullIf(this, BoxUtil.box(a));
  }

  // Basic arithmetic

  public NumericSyntaxExpression plus(final NumericExpression n) {
    return new Plus(this, n);
  }

  public NumericSyntaxExpression plus(final Number n) {
    return new Plus(this, BoxUtil.box(n));
  }

  public NumericSyntaxExpression minus(final NumericExpression n) {
    return new Minus(this, n);
  }

  public NumericSyntaxExpression minus(final Number n) {
    return new Minus(this, BoxUtil.box(n));
  }

  public NumericSyntaxExpression mult(final NumericExpression n) {
    return new Mult(this, n);
  }

  public NumericSyntaxExpression mult(final Number n) {
    return new Mult(this, BoxUtil.box(n));
  }

  public NumericSyntaxExpression div(final NumericExpression n) {
    return new Div(this, n);
  }

  public NumericSyntaxExpression div(final Number n) {
    return new Div(this, BoxUtil.box(n));
  }

  public NumericSyntaxExpression remainder(final NumericExpression n) {
    return new Remainder(this, n);
  }

  public NumericSyntaxExpression remainder(final Number n) {
    return new Remainder(this, BoxUtil.box(n));
  }

  public NumericSyntaxExpression pow(final NumericExpression exponent) {
    return new Power(this, exponent);
  }

  public NumericSyntaxExpression pow(final Number exponent) {
    return new Power(this, BoxUtil.box(exponent));
  }

  public NumericSyntaxExpression log(final NumericExpression base) {
    return new Log(this, base);
  }

  public NumericSyntaxExpression log(final Number base) {
    return new Log(this, new NumericConstant(base));
  }

  public NumericSyntaxExpression round() {
    return new Round(this, BoxUtil.box(0));
  }

  public NumericSyntaxExpression round(final NumericExpression places) {
    return new Round(this, places);
  }

  public NumericSyntaxExpression round(final Number places) {
    return new Round(this, BoxUtil.box(places));
  }

  public NumericSyntaxExpression trunc() {
    return new Trunc(this, BoxUtil.box(0));
  }

  public NumericSyntaxExpression trunc(final NumericExpression places) {
    return new Trunc(this, places);
  }

  public NumericSyntaxExpression trunc(final Number places) {
    return new Trunc(this, BoxUtil.box(places));
  }

  public NumericSyntaxExpression neg() {
    return new Neg(this);
  }

  public NumericSyntaxExpression abs() {
    return new Abs(this);
  }

  public NumericSyntaxExpression signum() {
    return new Signum(this);
  }

  // Scalar comparisons

  // Equal

  public BooleanSyntaxExpression eq(final NumericExpression e) {
    return new Equal(this, e);
  }

  public BooleanSyntaxExpression eq(final Number value) {
    return new Equal(this, BoxUtil.box(value));
  }

  // Not Equal

  public BooleanSyntaxExpression ne(final NumericExpression e) {
    return new NotEqual(this, e);
  }

  public BooleanSyntaxExpression ne(final Number value) {
    return new NotEqual(this, BoxUtil.box(value));
  }

  // Greater Than

  public BooleanSyntaxExpression gt(final NumericExpression e) {
    return new GreaterThan(this, e);
  }

  public BooleanSyntaxExpression gt(final Number value) {
    return new GreaterThan(this, BoxUtil.box(value));
  }

  // Greater Than or Equal To

  public BooleanSyntaxExpression ge(final NumericExpression e) {
    return new GreaterThanOrEqualTo(this, e);
  }

  public BooleanSyntaxExpression ge(final Number value) {
    return new GreaterThanOrEqualTo(this, BoxUtil.box(value));
  }

  // Less Than

  public BooleanSyntaxExpression lt(final NumericExpression e) {
    return new LessThan(this, e);
  }

  public BooleanSyntaxExpression lt(final Number value) {
    return new LessThan(this, BoxUtil.box(value));
  }

  // Less Than or Equal To

  public BooleanSyntaxExpression le(final NumericExpression e) {
    return new LessThanOrEqualTo(this, e);
  }

  public BooleanSyntaxExpression le(final Number value) {
    return new LessThanOrEqualTo(this, BoxUtil.box(value));
  }

  // Between

  public BooleanSyntaxExpression between(final NumericExpression from, final NumericExpression to) {
    return new Between(this, from, to);
  }

  public BooleanSyntaxExpression between(final NumericExpression from, final Number to) {
    return new Between(this, from, BoxUtil.box(to));
  }

  public BooleanSyntaxExpression between(final Number from, final NumericExpression to) {
    return new Between(this, BoxUtil.box(from), to);
  }

  public BooleanSyntaxExpression between(final Number from, final Number to) {
    return new Between(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  // Not Between

  public BooleanSyntaxExpression notBetween(final NumericExpression from, final NumericExpression to) {
    return new NotBetween<NumericExpression>(this, from, to);
  }

  public BooleanSyntaxExpression notBetween(final NumericExpression from, final Number to) {
    return new NotBetween<NumericExpression>(this, from, BoxUtil.box(to));
  }

  public BooleanSyntaxExpression notBetween(final Number from, final NumericExpression to) {
    return new NotBetween<NumericExpression>(this, BoxUtil.box(from), to);
  }

  public BooleanSyntaxExpression notBetween(final Number from, Number to) {
    return new NotBetween<NumericExpression>(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  // In list

  public final BooleanSyntaxExpression in(final NumericExpression... values) {
    return new InList<NumericExpression>(this, Arrays.asList(values));
  }

  public final BooleanSyntaxExpression in(final Number... values) {
    return new InList<NumericExpression>(this, Stream.of(values).map(v -> BoxUtil.box(v)).collect(Collectors.toList()));
  }

  public final BooleanSyntaxExpression notIn(final NumericExpression... values) {
    return new NotInList<NumericExpression>(this, Arrays.asList(values));
  }

  public final BooleanSyntaxExpression notIn(final Number... values) {
    return new NotInList<NumericExpression>(this,
        Stream.of(values).map(v -> BoxUtil.box(v)).collect(Collectors.toList()));
  }

}
