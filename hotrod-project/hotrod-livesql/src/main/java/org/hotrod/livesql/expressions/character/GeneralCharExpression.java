package org.hotrod.livesql.expressions.character;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hotrod.livesql.Available;
import org.hotrod.livesql.dialects.Const;
import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.bool.Between;
import org.hotrod.livesql.expressions.bool.Equal;
import org.hotrod.livesql.expressions.bool.GreaterThan;
import org.hotrod.livesql.expressions.bool.GreaterThanOrEqualTo;
import org.hotrod.livesql.expressions.bool.InList;
import org.hotrod.livesql.expressions.bool.LessThan;
import org.hotrod.livesql.expressions.bool.LessThanOrEqualTo;
import org.hotrod.livesql.expressions.bool.Like;
import org.hotrod.livesql.expressions.bool.NotBetween;
import org.hotrod.livesql.expressions.bool.NotEqual;
import org.hotrod.livesql.expressions.bool.NotInList;
import org.hotrod.livesql.expressions.bool.NotLike;
import org.hotrod.livesql.expressions.bool.Predicate;
import org.hotrod.livesql.expressions.character.postgresql.Ascii;
import org.hotrod.livesql.expressions.numeric.GeneralNumericExpression;
import org.hotrod.livesql.expressions.numeric.NumericConstant;
import org.hotrod.livesql.expressions.numeric.NumericExpression;
import org.hotrod.livesql.queries.subqueries.Subquery;
import org.hotrod.livesql.queries.subqueries.SubqueryCharRefColumn;
import org.hotrod.livesql.util.BoxUtil;

public abstract class GeneralCharExpression extends ComparableExpression {

  protected GeneralCharExpression(final int precedence) {
    super(precedence);
  }

  @Override
  protected Expression asSubqueryExpression(final Subquery subquery, final String alias) {
    return new SubqueryCharRefColumn(subquery, alias, this);
  }

  // Coalesce

  public CharExpression coalesce(final GeneralCharExpression a) {
    return new CharCoalesce(this, a);
  }

  public CharExpression coalesce(final String a) {
    return new CharCoalesce(this, new CharConstant(a));
  }

  // NullIf

  public CharExpression nullIf(final GeneralCharExpression a) {
    return new CharNullIf(this, a);
  }

  public CharExpression nullIf(final String a) {
    return new CharNullIf(this, new CharConstant(a));
  }

  // Locate

  public NumericExpression locate(final GeneralCharExpression substring, final GeneralNumericExpression from) {
    return new Locate(substring, this, from);
  }

  public NumericExpression locate(final GeneralCharExpression substring, final Number from) {
    return new Locate(substring, this, new NumericConstant(from));
  }

  public NumericExpression locate(final String substring, final GeneralNumericExpression from) {
    return new Locate(new CharConstant(substring), this, from);
  }

  public NumericExpression locate(final String substring, final Number from) {
    return new Locate(new CharConstant(substring), this, new NumericConstant(from));
  }

  public NumericExpression locate(final GeneralCharExpression substring) {
    return new Locate(substring, this, new NumericConstant(0));
  }

  public NumericExpression locate(final String substring) {
    return new Locate(new CharConstant(substring), this, new NumericConstant(0));
  }

  // Substr

  public CharExpression substr(final GeneralNumericExpression from, final GeneralNumericExpression length) {
    return new Substring(this, from, length);
  }

  public CharExpression substr(final GeneralNumericExpression from, final Number length) {
    return new Substring(this, from, new NumericConstant(length));
  }

  public CharExpression substr(Number from, final GeneralNumericExpression length) {
    return new Substring(this, new NumericConstant(from), length);
  }

  public CharExpression substr(Number from, final Number length) {
    return new Substring(this, new NumericConstant(from), new NumericConstant(length));
  }

  public CharExpression substr(final GeneralNumericExpression from) {
    return new Substring(this, from);
  }

  public CharExpression substr(final Number from) {
    return new Substring(this, new NumericConstant(from));
  }

  // General functions

  public CharExpression concat(final GeneralCharExpression e) {
    Concat concat = new Concat(this, e);
    return concat;
  }

  public CharExpression concat(final String e) {
    Concat concat = new Concat(this, new CharConstant(e));
    return concat;
  }

  public NumericExpression length() {
    return new Length(this);
  }

  public CharExpression lower() {
    return new Lower(this);
  }

  public CharExpression upper() {
    return new Upper(this);
  }

  public CharExpression trim() {
    return new Trim(this);
  }

  // Like

  public Predicate like(final GeneralCharExpression e) {
    return new Like(this, e);
  }

  public Predicate like(final String value) {
    return new Like(this, new CharConstant(value));
  }

  // Like escape

  public Predicate like(final GeneralCharExpression e, final GeneralCharExpression escape) {
    return new Like(this, e, escape);
  }

  public Predicate like(final GeneralCharExpression e, final String escape) {
    return new Like(this, e, new CharConstant(escape));
  }

  public Predicate like(final String e, final GeneralCharExpression escape) {
    return new Like(this, new CharConstant(e), escape);
  }

  public Predicate like(final String e, final String escape) {
    return new Like(this, new CharConstant(e), new CharConstant(escape));
  }

  // Not Like

  public Predicate notLike(final GeneralCharExpression e) {
    return new NotLike(this, e);
  }

  public Predicate notLike(final String e) {
    return new NotLike(this, new CharConstant(e));
  }

  // Not like escape

  public Predicate notLike(final GeneralCharExpression e, final GeneralCharExpression escape) {
    return new NotLike(this, e, escape);
  }

  public Predicate notLike(final GeneralCharExpression e, final String escape) {
    return new NotLike(this, e, new CharConstant(escape));
  }

  public Predicate notLike(final String e, final GeneralCharExpression escape) {
    return new NotLike(this, new CharConstant(e), escape);
  }

  public Predicate notLike(final String e, final String escape) {
    return new NotLike(this, new CharConstant(e), new CharConstant(escape));
  }

  // TODO: implement in subclasses

  // Scalar comparisons

  // Equal

  public Predicate eq(final GeneralCharExpression e) {
    return new Equal(this, e);
  }

  public Predicate eq(final String value) {
    return new Equal(this, BoxUtil.box(value));
  }

  // Not Equal

  public Predicate ne(final GeneralCharExpression e) {
    return new NotEqual(this, e);
  }

  public Predicate ne(final String value) {
    return new NotEqual(this, BoxUtil.box(value));
  }

  // Greater Than

  public Predicate gt(final GeneralCharExpression e) {
    return new GreaterThan(this, e);
  }

  public Predicate gt(final String value) {
    return new GreaterThan(this, BoxUtil.box(value));
  }

  // Greater Than or Equal To

  public Predicate ge(final GeneralCharExpression e) {
    return new GreaterThanOrEqualTo(this, e);
  }

  public Predicate ge(final String value) {
    return new GreaterThanOrEqualTo(this, BoxUtil.box(value));
  }

  // Less Than

  public Predicate lt(final GeneralCharExpression e) {
    return new LessThan(this, e);
  }

  public Predicate lt(final String value) {
    return new LessThan(this, BoxUtil.box(value));
  }

  // Less Than or Equal To

  public Predicate le(final GeneralCharExpression e) {
    return new LessThanOrEqualTo(this, e);
  }

  public Predicate le(final String value) {
    return new LessThanOrEqualTo(this, BoxUtil.box(value));
  }

  // Between

  public Predicate between(final GeneralCharExpression from, final GeneralCharExpression to) {
    return new Between(this, from, to);
  }

  public Predicate between(final GeneralCharExpression from, final String to) {
    return new Between(this, from, BoxUtil.box(to));
  }

  public Predicate between(final String from, final GeneralCharExpression to) {
    return new Between(this, BoxUtil.box(from), to);
  }

  public Predicate between(final String from, final String to) {
    return new Between(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  // Not Between

  public Predicate notBetween(final GeneralCharExpression from, final GeneralCharExpression to) {
    return new NotBetween<GeneralCharExpression>(this, from, to);
  }

  public Predicate notBetween(final GeneralCharExpression from, final String to) {
    return new NotBetween<GeneralCharExpression>(this, from, BoxUtil.box(to));
  }

  public Predicate notBetween(final String from, final GeneralCharExpression to) {
    return new NotBetween<GeneralCharExpression>(this, BoxUtil.box(from), to);
  }

  public Predicate notBetween(final String from, String to) {
    return new NotBetween<GeneralCharExpression>(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  // In list

  public final Predicate in(final GeneralCharExpression... values) {
    return new InList<GeneralCharExpression>(this, Arrays.asList(values));
  }

  public final Predicate in(final String... values) {
    return new InList<GeneralCharExpression>(this,
        Stream.of(values).map(v -> BoxUtil.box(v)).collect(Collectors.toList()));
  }

  public final Predicate notIn(final GeneralCharExpression... values) {
    return new NotInList<GeneralCharExpression>(this, Arrays.asList(values));
  }

  public final Predicate notIn(final String... values) {
    return new NotInList<GeneralCharExpression>(this,
        Stream.of(values).map(v -> BoxUtil.box(v)).collect(Collectors.toList()));
  }

  // TODO: END -- implement in subclasses

  // Specialized Functions

  @Available(engine = Const.POSTGRESQL, since = Const.PG15)
  public final GeneralNumericExpression ascii() {
    return new Ascii(this);
  }

}
