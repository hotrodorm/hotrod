package org.hotrod.livesql.expressions.character;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hotrod.livesql.Available;
import org.hotrod.livesql.dialects.Const;
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
import org.hotrod.livesql.expressions.bool.Like;
import org.hotrod.livesql.expressions.bool.NotBetween;
import org.hotrod.livesql.expressions.bool.NotEqual;
import org.hotrod.livesql.expressions.bool.NotInList;
import org.hotrod.livesql.expressions.bool.NotLike;
import org.hotrod.livesql.expressions.character.postgresql.Ascii;
import org.hotrod.livesql.expressions.numeric.NumericConstant;
import org.hotrod.livesql.expressions.numeric.NumericExpression;
import org.hotrod.livesql.expressions.numeric.NumericSyntaxExpression;
import org.hotrod.livesql.queries.subqueries.CharSubqueryExpression;
import org.hotrod.livesql.queries.subqueries.Subquery;
import org.hotrod.livesql.util.BoxUtil;

public abstract class CharExpression extends ComparableExpression {

  protected CharExpression(final int precedence) {
    super(precedence);
  }

  @Override
  protected Expression asSubqueryExpression(final Subquery subquery, final String alias) {
    return new CharSubqueryExpression(subquery, alias, this);
  }

  // Coalesce

  public CharSyntaxExpression coalesce(final CharExpression a) {
    return new CharCoalesce(this, a);
  }

  public CharSyntaxExpression coalesce(final String a) {
    return new CharCoalesce(this, new CharConstant(a));
  }

  // NullIf

  public CharSyntaxExpression nullIf(final CharExpression a) {
    return new CharNullIf(this, a);
  }

  public CharSyntaxExpression nullIf(final String a) {
    return new CharNullIf(this, new CharConstant(a));
  }

  // Locate

  public NumericSyntaxExpression locate(final CharExpression substring, final NumericExpression from) {
    return new Locate(substring, this, from);
  }

  public NumericSyntaxExpression locate(final CharExpression substring, final Number from) {
    return new Locate(substring, this, new NumericConstant(from));
  }

  public NumericSyntaxExpression locate(final String substring, final NumericExpression from) {
    return new Locate(new CharConstant(substring), this, from);
  }

  public NumericSyntaxExpression locate(final String substring, final Number from) {
    return new Locate(new CharConstant(substring), this, new NumericConstant(from));
  }

  public NumericSyntaxExpression locate(final CharExpression substring) {
    return new Locate(substring, this, new NumericConstant(0));
  }

  public NumericSyntaxExpression locate(final String substring) {
    return new Locate(new CharConstant(substring), this, new NumericConstant(0));
  }

  // Substr

  public CharSyntaxExpression substr(final NumericExpression from, final NumericExpression length) {
    return new Substring(this, from, length);
  }

  public CharSyntaxExpression substr(final NumericExpression from, final Number length) {
    return new Substring(this, from, new NumericConstant(length));
  }

  public CharSyntaxExpression substr(Number from, final NumericExpression length) {
    return new Substring(this, new NumericConstant(from), length);
  }

  public CharSyntaxExpression substr(Number from, final Number length) {
    return new Substring(this, new NumericConstant(from), new NumericConstant(length));
  }

  public CharSyntaxExpression substr(final NumericExpression from) {
    return new Substring(this, from);
  }

  public CharSyntaxExpression substr(final Number from) {
    return new Substring(this, new NumericConstant(from));
  }

  // General functions

  public CharSyntaxExpression concat(final CharExpression e) {
    Concat concat = new Concat(this, e);
    return concat;
  }

  public CharSyntaxExpression concat(final String e) {
    Concat concat = new Concat(this, new CharConstant(e));
    return concat;
  }

  public NumericSyntaxExpression length() {
    return new Length(this);
  }

  public CharSyntaxExpression lower() {
    return new Lower(this);
  }

  public CharSyntaxExpression upper() {
    return new Upper(this);
  }

  public CharSyntaxExpression trim() {
    return new Trim(this);
  }

  // Like

  public BooleanSyntaxExpression like(final CharExpression e) {
    return new Like(this, e);
  }

  public BooleanSyntaxExpression like(final String value) {
    return new Like(this, new CharConstant(value));
  }

  // Like escape

  public BooleanSyntaxExpression like(final CharExpression e, final CharExpression escape) {
    return new Like(this, e, escape);
  }

  public BooleanSyntaxExpression like(final CharExpression e, final String escape) {
    return new Like(this, e, new CharConstant(escape));
  }

  public BooleanSyntaxExpression like(final String e, final CharExpression escape) {
    return new Like(this, new CharConstant(e), escape);
  }

  public BooleanSyntaxExpression like(final String e, final String escape) {
    return new Like(this, new CharConstant(e), new CharConstant(escape));
  }

  // Not Like

  public BooleanSyntaxExpression notLike(final CharExpression e) {
    return new NotLike(this, e);
  }

  public BooleanSyntaxExpression notLike(final String e) {
    return new NotLike(this, new CharConstant(e));
  }

  // Not like escape

  public BooleanSyntaxExpression notLike(final CharExpression e, final CharExpression escape) {
    return new NotLike(this, e, escape);
  }

  public BooleanSyntaxExpression notLike(final CharExpression e, final String escape) {
    return new NotLike(this, e, new CharConstant(escape));
  }

  public BooleanSyntaxExpression notLike(final String e, final CharExpression escape) {
    return new NotLike(this, new CharConstant(e), escape);
  }

  public BooleanSyntaxExpression notLike(final String e, final String escape) {
    return new NotLike(this, new CharConstant(e), new CharConstant(escape));
  }

  // TODO: implement in subclasses

  // Scalar comparisons

  // Equal

  public BooleanSyntaxExpression eq(final CharExpression e) {
    return new Equal(this, e);
  }

  public BooleanSyntaxExpression eq(final String value) {
    return new Equal(this, BoxUtil.box(value));
  }

  // Not Equal

  public BooleanSyntaxExpression ne(final CharExpression e) {
    return new NotEqual(this, e);
  }

  public BooleanSyntaxExpression ne(final String value) {
    return new NotEqual(this, BoxUtil.box(value));
  }

  // Greater Than

  public BooleanSyntaxExpression gt(final CharExpression e) {
    return new GreaterThan(this, e);
  }

  public BooleanSyntaxExpression gt(final String value) {
    return new GreaterThan(this, BoxUtil.box(value));
  }

  // Greater Than or Equal To

  public BooleanSyntaxExpression ge(final CharExpression e) {
    return new GreaterThanOrEqualTo(this, e);
  }

  public BooleanSyntaxExpression ge(final String value) {
    return new GreaterThanOrEqualTo(this, BoxUtil.box(value));
  }

  // Less Than

  public BooleanSyntaxExpression lt(final CharExpression e) {
    return new LessThan(this, e);
  }

  public BooleanSyntaxExpression lt(final String value) {
    return new LessThan(this, BoxUtil.box(value));
  }

  // Less Than or Equal To

  public BooleanSyntaxExpression le(final CharExpression e) {
    return new LessThanOrEqualTo(this, e);
  }

  public BooleanSyntaxExpression le(final String value) {
    return new LessThanOrEqualTo(this, BoxUtil.box(value));
  }

  // Between

  public BooleanSyntaxExpression between(final CharExpression from, final CharExpression to) {
    return new Between(this, from, to);
  }

  public BooleanSyntaxExpression between(final CharExpression from, final String to) {
    return new Between(this, from, BoxUtil.box(to));
  }

  public BooleanSyntaxExpression between(final String from, final CharExpression to) {
    return new Between(this, BoxUtil.box(from), to);
  }

  public BooleanSyntaxExpression between(final String from, final String to) {
    return new Between(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  // Not Between

  public BooleanSyntaxExpression notBetween(final CharExpression from, final CharExpression to) {
    return new NotBetween<CharExpression>(this, from, to);
  }

  public BooleanSyntaxExpression notBetween(final CharExpression from, final String to) {
    return new NotBetween<CharExpression>(this, from, BoxUtil.box(to));
  }

  public BooleanSyntaxExpression notBetween(final String from, final CharExpression to) {
    return new NotBetween<CharExpression>(this, BoxUtil.box(from), to);
  }

  public BooleanSyntaxExpression notBetween(final String from, String to) {
    return new NotBetween<CharExpression>(this, BoxUtil.box(from), BoxUtil.box(to));
  }

  // In list

  public final BooleanSyntaxExpression in(final CharExpression... values) {
    return new InList<CharExpression>(this, Arrays.asList(values));
  }

  public final BooleanSyntaxExpression in(final String... values) {
    return new InList<CharExpression>(this, Stream.of(values).map(v -> BoxUtil.box(v)).collect(Collectors.toList()));
  }

  public final BooleanSyntaxExpression notIn(final CharExpression... values) {
    return new NotInList<CharExpression>(this, Arrays.asList(values));
  }

  public final BooleanSyntaxExpression notIn(final String... values) {
    return new NotInList<CharExpression>(this, Stream.of(values).map(v -> BoxUtil.box(v)).collect(Collectors.toList()));
  }

  // TODO: END -- implement in subclasses

  // Specialized Functions

  @Available(engine = Const.POSTGRESQL, since = Const.PG15)
  public final NumericExpression ascii() {
    return new Ascii(this);
  }

}
